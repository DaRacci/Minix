package dev.racci.paperweight.mpp

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.papermc.paperweight.DownloadService
import io.papermc.paperweight.tasks.RemapJar
import io.papermc.paperweight.userdev.attribute.Obfuscation
import io.papermc.paperweight.util.cache
import io.papermc.paperweight.util.constants.DECOMPILER_CONFIG
import io.papermc.paperweight.util.constants.DECOMPILER_REPO_NAME
import io.papermc.paperweight.util.constants.DEOBF_NAMESPACE
import io.papermc.paperweight.util.constants.DEV_BUNDLE_CONFIG
import io.papermc.paperweight.util.constants.MOJANG_MAPPED_SERVER_CONFIG
import io.papermc.paperweight.util.constants.MOJANG_MAPPED_SERVER_RUNTIME_CONFIG
import io.papermc.paperweight.util.constants.PAPERWEIGHT_EXTENSION
import io.papermc.paperweight.util.constants.PAPER_MAVEN_REPO_URL
import io.papermc.paperweight.util.constants.PARAM_MAPPINGS_CONFIG
import io.papermc.paperweight.util.constants.PARAM_MAPPINGS_REPO_NAME
import io.papermc.paperweight.util.constants.REMAPPER_CONFIG
import io.papermc.paperweight.util.constants.REMAPPER_REPO_NAME
import io.papermc.paperweight.util.constants.REOBF_CONFIG
import io.papermc.paperweight.util.constants.SPIGOT_NAMESPACE
import io.papermc.paperweight.util.pathProvider
import io.papermc.paperweight.util.providerFor
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.Bundling
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.attributes.Usage
import org.gradle.api.tasks.Delete
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.registerIfAbsent
import org.gradle.kotlin.dsl.repositories
import org.gradle.util.internal.NameMatcher
import org.gradle.workers.WorkerExecutor
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmCompilation
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.slf4j.LoggerFactory
import javax.inject.Inject
import kotlin.collections.any
import kotlin.collections.forEach
import kotlin.collections.mapNotNull
import kotlin.collections.mutableMapOf

public abstract class PaperweightMppPlugin : Plugin<Project> {
    @get:Inject
    public abstract val workerExecutor: WorkerExecutor

    @get:Inject
    public abstract val javaToolchainService: JavaToolchainService

    override fun apply(target: Project): Unit = with(target) {
        instance = this@PaperweightMppPlugin
        logger.info("Applying PaperweightMPP plugin to project ${target.path}")

        target.gradle.sharedServices.registerIfAbsent("download", DownloadService::class) {}

        createRootTasks(project)

        project.kotlinMPP().targets.all {
            if (this !is KotlinJvmTarget) {
                logger.info("Skipping non-JVM target $name")
                return@all
            } else {
                logger.info("Configuring target $name for PaperweightMPP")
            }

            configureTasks()
        }
    }

    public companion object {
        public const val PAPERWEIGHT_GROUP: String = "paperweight"
        public const val REOBF_JAR_TASK: String = "reobfJar"
        public const val CLEAN_CACHE_TASK: String = "cleanCache"

        private val logger = LoggerFactory.getLogger(this::class.java)
        private lateinit var instance: PaperweightMppPlugin

        internal val USERDEV = mutableMapOf<KotlinSourceSet, UserdevSetupMPP>()

        internal fun configureKotlinCompilation(
            sourceSet: KotlinSourceSet,
            project: Project,
            userdev: UserdevSetupMPP
        ) = with(sourceSet) {
            logger.info("Configuring source set $name ($groupName) for PaperweightMPP (Configure Kotlin Compilation)")

            val userdevExtension = project.extensions.create<PaperweightUserExtensionMPP>(
                disambiguateName(PAPERWEIGHT_EXTENSION),
                this,
                project.objects
            )

            createConfigurations(this, project, userdev)

            project.afterEvaluate {
                logger.info("Configuring source set $name ($groupName) for PaperweightMPP (AfterEvaluate)")

                if (isCleaningCache()) {
                    logger.info(
                        "Skipping source set $name ($groupName) for PaperweightMPP because cleanCache is running"
                    )
                    return@afterEvaluate
                } else {
                    logger.info("Configuring source set $name ($groupName) for PaperweightMPP (Not cleaning cache)")
                }

                val target = runCatching { project.kotlinMPP().targets.getByName(groupName) as KotlinJvmTarget }
                    .getOrElse {
                        throw GradleException(
                            "Unable to get the target for source $sourceSet with group $groupName (Implicit via name)"
                        )
                    }

                userdevExtension.reobfArtifactConfiguration.get().configure(
                    target,
                    project.tasks.providerFor(target.disambiguateName(REOBF_JAR_TASK))
                )

                if (userdevExtension.injectPaperRepository.get()) {
                    logger.info("Injecting Paper repository for source set $name ($groupName)")
                    project.repositories.maven(PAPER_MAVEN_REPO_URL) {
                        content {
                            onlyForConfigurations(
                                disambiguateName(DEV_BUNDLE_CONFIG)
                            )
                        }
                    }
                }

                configureRepositories(sourceSet, userdev)
            }
        }

        private fun configureRepositories(
            sourceSet: KotlinSourceSet,
            userdevSetup: UserdevSetupMPP
        ) = with(sourceSet) {
            userdevSetup.cachedProject.repositories {
                maven(userdevSetup.paramMappings.url) {
                    name = PARAM_MAPPINGS_REPO_NAME
                    content { onlyForConfigurations(disambiguateName(PARAM_MAPPINGS_CONFIG)) }
                }
                maven(userdevSetup.remapper.url) {
                    name = REMAPPER_REPO_NAME
                    content { onlyForConfigurations(disambiguateName(REMAPPER_CONFIG)) }
                }
                maven(userdevSetup.decompiler.url) {
                    name = DECOMPILER_REPO_NAME
                    content { onlyForConfigurations(disambiguateName(DECOMPILER_CONFIG)) }
                }

                userdevSetup.libraryRepositories.forEach {
                    logger.info("Adding library repository $it for source set $name ($groupName)")
                    maven(it)
                }
                userdevSetup.addIvyRepository()
            }
        }

        private fun createConfigurations(
            sourceSet: KotlinSourceSet,
            project: Project,
            userdev: UserdevSetupMPP
        ) = with(sourceSet) {
            logger.info("Creating configurations for source set $name ($groupName) for PaperweightMPP")

            project.configurations.create(disambiguateName(DECOMPILER_CONFIG)) {
                defaultDependencies {
                    for (dep in userdev.decompiler.coordinates) {
                        add(project.dependencies.create(dep))
                    }
                }
            }

            project.configurations.create(disambiguateName(PARAM_MAPPINGS_CONFIG)) {
                defaultDependencies {
                    for (dep in userdev.paramMappings.coordinates) {
                        add(project.dependencies.create(dep))
                    }
                }
            }

            project.configurations.create(disambiguateName(REMAPPER_CONFIG)) {
                isTransitive = false // we use a fat jar for tiny-remapper, so we don't need it's transitive deps
                defaultDependencies {
                    for (dep in userdev.remapper.coordinates) {
                        add(project.dependencies.create(dep))
                    }
                }
            }

            project.configurations.create(disambiguateName(MOJANG_MAPPED_SERVER_CONFIG)) {
                exclude("junit", "junit") // json-simple exposes junit for some reason
                defaultDependencies {
                    val ctx = createContext(project, sourceSet)
                    userdev.createOrUpdateIvyRepository(ctx)
                    userdev.populateCompileConfiguration(ctx, this)
                }

                isTransitive = false
                println(compileOnlyConfigurationName)
                project.configurations[compileOnlyConfigurationName].extendsFrom(this)
            }

            project.configurations.create(disambiguateName(MOJANG_MAPPED_SERVER_RUNTIME_CONFIG)) {
                defaultDependencies {
                    val ctx = createContext(project, sourceSet)
                    userdev.createOrUpdateIvyRepository(ctx)
                    userdev.populateRuntimeConfiguration(ctx, this)
                }
            }
        }

        private fun createContext(
            project: Project,
            source: KotlinSourceSet
        ): SetupHandlerMPP.Context = SetupHandlerMPP.Context(
            project,
            source,
            instance.workerExecutor,
            instance.javaToolchainService
        )

        private fun KotlinJvmTarget.configureTasks() {
            val (relatedSourceSet, userdev) = compilations.flatMap(KotlinJvmCompilation::kotlinSourceSets)
                .mapNotNull { set -> set to (USERDEV[set] ?: return@mapNotNull null) }
                .takeUnless { it.isEmpty() || it.size > 1 }?.first()
                ?: error("Expected exactly one userdev source set, but found ${USERDEV.size}")

            logger.info("Configuring target $name with tasks for PaperweightMPP")

            val reobfJar = project.tasks.register<RemapJar>(disambiguateName(REOBF_JAR_TASK)) {
                group = PAPERWEIGHT_GROUP
                description = "Remap the compiled plugin jar to Spigot's obfuscated runtime names. ($name MPP target)"
                fromNamespace.set(DEOBF_NAMESPACE)
                toNamespace.set(SPIGOT_NAMESPACE)

                mappingsFile.pathProvider(project.provider { userdev.reobfMappings })
                remapClasspath.from(project.provider { userdev.serverJar })

                remapper.from(project.configurations[relatedSourceSet.disambiguateName(REMAPPER_CONFIG)])
                remapperArgs.set(userdev.pluginRemapArgs)

                inputJar.set(project.tasks.named<ShadowJar>(disambiguateName("shadowJar")).get().archiveFile)
            }

            project.tasks.create<Delete>(disambiguateName(CLEAN_CACHE_TASK)) {
                group = PAPERWEIGHT_GROUP
                description = "Deletes the paperweight cache. ($name MPP target)"

                delete(project.layout.cache.resolve(name).toFile())
            }.also { project.tasks.named(CLEAN_CACHE_TASK).get().dependsOn(it) }

            project.configurations.register(disambiguateName(REOBF_CONFIG)) {
                isCanBeConsumed = true
                isCanBeResolved = false
                attributes { // Maybe needs change?
                    attribute(Usage.USAGE_ATTRIBUTE, project.objects.named(Usage.JAVA_RUNTIME))
                    attribute(Category.CATEGORY_ATTRIBUTE, project.objects.named(Category.LIBRARY))
                    attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, project.objects.named(LibraryElements.JAR))
                    attribute(Bundling.BUNDLING_ATTRIBUTE, project.objects.named(Bundling.EXTERNAL))
                    attribute(Obfuscation.OBFUSCATION_ATTRIBUTE, project.objects.named(Obfuscation.OBFUSCATED))
                }
                outgoing.artifact(reobfJar.get().outputJar)
            }
        }

        internal fun createRootTasks(project: Project) = with(project) {
            tasks.register<Delete>(CLEAN_CACHE_TASK) {
                group = PAPERWEIGHT_GROUP
                description = "Deletes the paperweight cache"
                delete(layout.cache)
            } /* to tasks.register<RemapJar>(REOBF_JAR_TASK) {
                group = PAPERWEIGHT_GROUP
                description = "Remap the compiled plugin jar to Spigot's obfuscated runtime names."
                fromNamespace.set(DEOBF_NAMESPACE)
                toNamespace.set(SPIGOT_NAMESPACE)
            }*/
        }

        private fun Project.isCleaningCache(): Boolean {
            // Manually check if cleanCache is a target, and skip setup.
            // Gradle moved NameMatcher to internal packages in 7.1, so this solution isn't ideal,
            // but it does work and allows using the cleanCache task without setting up the workspace first.
            return gradle.startParameter.taskRequests.any { req ->
                req.args.any { arg -> NameMatcher().find(arg, tasks.names) == CLEAN_CACHE_TASK }
            }
        }
    }
}
