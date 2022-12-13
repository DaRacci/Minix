package dev.racci.paperweight.mpp

import io.papermc.paperweight.DownloadService
import io.papermc.paperweight.PaperweightException
import io.papermc.paperweight.tasks.RemapJar
import io.papermc.paperweight.userdev.attribute.Obfuscation
import io.papermc.paperweight.userdev.internal.setup.util.genSources
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
import io.papermc.paperweight.util.convertToPath
import io.papermc.paperweight.util.download
import io.papermc.paperweight.util.pathProvider
import io.papermc.paperweight.util.providerFor
import io.papermc.paperweight.util.set
import io.papermc.paperweight.util.sha256asHex
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.Bundling
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.attributes.Usage
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.TaskProvider
import org.gradle.internal.impldep.org.glassfish.jaxb.runtime.v2.ContextFactory.createContext
import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.kotlin.dsl.* // ktlint-disable no-wildcard-imports
import org.gradle.util.internal.NameMatcher
import org.gradle.workers.WorkerExecutor
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import java.util.Optional
import javax.inject.Inject

abstract class PaperweightMppPlugin : Plugin<Project> {
    @get:Inject
    abstract val workerExecutor: WorkerExecutor

    @get:Inject
    abstract val javaToolchainService: JavaToolchainService

    override fun apply(target: Project): Unit = with(target) {
        target.gradle.sharedServices.registerIfAbsent("download", DownloadService::class) {}

        val cleanCache = target.tasks.register<Delete>("cleanCache") {
            group = "paperweight"
            description = "Deletes the paperweight cache"
            delete(target.layout.cache)
        }

        val reobfJar = target.tasks.register<RemapJar>("reobfJar") {
            group = "paperweight"
            description = "Remap the compiled plugin jar to Spigot's obfuscated runtime names."
            fromNamespace.set(DEOBF_NAMESPACE)
            toNamespace.set(SPIGOT_NAMESPACE)
        }

        project.kotlinMPP().sourceSets.whenObjectAdded {
            project.configurations.register(prefixedName(DEV_BUNDLE_CONFIG))
        }

        project.kotlinMPP().targets.whenObjectAdded {
            println(this.defaultConfigurationName)
        }

        project.kotlinMPP().targets.whenObjectAdded {
            project.configurations["${name}CompileClasspath"].exclude(
                module = "purpur-server"
            )
        }

        project.afterEvaluate {
            project.kotlinMPP().sourceSets.all {
                configureKotlinCompilation(project, cleanCache, reobfJar)
            }
        }
    }

    private fun KotlinSourceSet.configureKotlinCompilation(
        project: Project,
        commonCleanCache: TaskProvider<Delete>,
        commonReobfJar: TaskProvider<RemapJar>
    ) {
//        project.configurations.register(prefixedName(DEV_BUNDLE_CONFIG))

        // these must not be initialized until afterEvaluate, as they resolve the dev bundle
        val userdevSetup by lazy {
            val devBundleZip = runCatching { project.configurations.named(prefixedName(DEV_BUNDLE_CONFIG)).map { it.singleFile }.convertToPath() }
            if (devBundleZip.isFailure) return@lazy Optional.empty<UserdevSetupMPP>()
            val serviceName = "paperweight-userdev:setupService:${devBundleZip.getOrThrow().sha256asHex()}"

            project.gradle.sharedServices.registerIfAbsent(serviceName, UserdevSetupMPP::class) {
                parameters {
                    cache.set(project.layout.cache.resolve(name).toFile())
                    bundleZip.set(devBundleZip.getOrThrow())
                    downloadService.set(project.download)
                    genSources.set(project.genSources) // Might need change
                }
            }.get().also { it.setup(project) }.let { Optional.of(it) }
        }

        val userdev = project.extensions.create(
            prefixedName(PAPERWEIGHT_EXTENSION),
            PaperweightUserExtensionMPP::class,
            project,
            workerExecutor,
            javaToolchainService,
            project.provider { userdevSetup },
            project.objects
        )

        createConfigurations(project, project.provider { userdevSetup })
        configureTasks(project, project.provider { userdevSetup }, commonCleanCache, commonReobfJar)

        project.afterEvaluate {
            // Manually check if cleanCache is a target, and skip setup.
            // Gradle moved NameMatcher to internal packages in 7.1, so this solution isn't ideal,
            // but it does work and allows using the cleanCache task without setting up the workspace first
            val cleaningCache = gradle.startParameter.taskRequests.any { req ->
                req.args.any { arg -> NameMatcher().find(arg, tasks.names) == commonCleanCache.name }
            }
            if (cleaningCache) {
                return@afterEvaluate
            }

            userdev.reobfArtifactConfiguration.get().configure(project, project.tasks.providerFor(prefixedName(commonReobfJar.name)))

            if (userdev.injectPaperRepository.get()) {
                project.repositories.maven(PAPER_MAVEN_REPO_URL) { content { onlyForConfigurations(prefixedName(DEV_BUNDLE_CONFIG)) } }
            }

            // Print a friendly error message if the dev bundle is missing before we call anything else that will try and resolve it
            checkForDevBundle(this)
            configureRepositories(userdevSetup)
        }
    }

    private fun KotlinSourceSet.configureRepositories(userdevSetup: Optional<UserdevSetupMPP>) = userdevSetup.ifPresent { userdev ->
        userdev.cachedProject.repositories {
            maven(userdev.paramMappings.url) {
                name = PARAM_MAPPINGS_REPO_NAME
                content { onlyForConfigurations(prefixedName(PARAM_MAPPINGS_CONFIG)) }
            }
            maven(userdev.remapper.url) {
                name = REMAPPER_REPO_NAME
                content { onlyForConfigurations(prefixedName(REMAPPER_CONFIG)) }
            }
            maven(userdev.decompiler.url) {
                name = DECOMPILER_REPO_NAME
                content { onlyForConfigurations(prefixedName(DECOMPILER_CONFIG)) }
            }

            userdev.libraryRepositories.forEach(::maven)
            userdev.addIvyRepository()
        }
    }

    private fun KotlinSourceSet.checkForDevBundle(project: Project) {
        // TODO: Check if atleast one exists else fail.
        return
        val hasDevBundle = runCatching { !project.configurations.getByName(prefixedName(DEV_BUNDLE_CONFIG)).isEmpty }
        if (hasDevBundle.isFailure || !hasDevBundle.getOrThrow()) {
            val message = "paperweight requires a development bundle to be added to the 'paperweightDevelopmentBundle' configuration, as" +
                " well as a repository to resolve it from in order to function. Use the paperweightDevBundle extension function to do this easily."

            throw PaperweightException(
                message,
                hasDevBundle.exceptionOrNull()?.let { PaperweightException("Failed to resolve dev bundle", it) }
            )
        }
    }

    private fun Provider<Optional<UserdevSetupMPP>>.foldWithDev(
        ifAbsent: () -> Unit = {},
        ifPresent: (userdev: UserdevSetupMPP) -> Unit
    ) {
        if (this.get().isEmpty) return ifAbsent()
        ifPresent(this.get().get())
    }

    private fun KotlinSourceSet.createConfigurations(
        project: Project,
        userdevSetup: Provider<Optional<UserdevSetupMPP>>
    ) {
        project.configurations.register(prefixedName(DECOMPILER_CONFIG)) {
            defaultDependencies {
                userdevSetup.foldWithDev { userdev ->
                    for (dep in userdev.decompiler.coordinates) {
                        add(project.dependencies.create(dep))
                    }
                }
            }
        }

        project.configurations.register(prefixedName(PARAM_MAPPINGS_CONFIG)) {
            defaultDependencies {
                userdevSetup.foldWithDev { userdev ->
                    for (dep in userdev.paramMappings.coordinates) {
                        add(project.dependencies.create(dep))
                    }
                }
            }
        }

        project.configurations.register(prefixedName(REMAPPER_CONFIG)) {
            isTransitive = false // we use a fat jar for tiny-remapper, so we don't need it's transitive deps
            defaultDependencies {
                userdevSetup.foldWithDev { userdev ->
                    for (dep in userdev.remapper.coordinates) {
                        add(project.dependencies.create(dep))
                    }
                }
            }
        }

        val mojangMappedServerConfig = project.configurations.register(prefixedName(MOJANG_MAPPED_SERVER_CONFIG)) {
            exclude("junit", "junit") // json-simple exposes junit for some reason
            defaultDependencies {
                userdevSetup.foldWithDev { userdev ->
                    val ctx = createContext(project, this@createConfigurations)
                    userdev.createOrUpdateIvyRepository(ctx)
                    userdev.populateCompileConfiguration(ctx, this)
                }
            }
        }

        listOf(
            this.compileOnlyConfigurationName
            // Tests?
        ).map(project.configurations::named).forEach { config ->
            config.configure { extendsFrom(mojangMappedServerConfig.get()) }
        }

        project.configurations.register(prefixedName(MOJANG_MAPPED_SERVER_RUNTIME_CONFIG)) {
            defaultDependencies {
                userdevSetup.foldWithDev { userdev ->
                    val ctx = createContext(project, this@createConfigurations)
                    userdev.createOrUpdateIvyRepository(ctx)
                    userdev.populateRuntimeConfiguration(ctx, this)
                }
            }
        }
    }

    private fun createContext(
        project: Project,
        source: KotlinSourceSet
    ): SetupHandlerMPP.Context = SetupHandlerMPP.Context(project, source, workerExecutor, javaToolchainService)

    private fun KotlinSourceSet.configureTasks(
        project: Project,
        userdevSetup: Provider<Optional<UserdevSetupMPP>>,
        commonCleanCache: TaskProvider<Delete>,
        commonReobfJar: TaskProvider<RemapJar>
    ) {
        val reobfJar by project.tasks.register<RemapJar>(prefixedName(commonReobfJar.name)) {
            userdevSetup.foldWithDev({ enabled = false }) { userdev ->
                mappingsFile.pathProvider(project.provider { userdev.reobfMappings })
                remapClasspath.from(project.provider { userdev.serverJar })

                remapper.from(project.configurations.named(prefixedName(REMAPPER_CONFIG)))
                remapperArgs.set(project.provider { userdev.pluginRemapArgs })
            }
        }.also { commonReobfJar.get().dependsOn(it) }

        project.tasks.register<Delete>(prefixedName(commonCleanCache.name)) {
            userdevSetup.foldWithDev({ enabled = false }) { _ ->
                delete(project.layout.cache.resolve(name).toFile())
            }
        }.also { commonCleanCache.get().dependsOn(it) }

        project.configurations.register(prefixedName(REOBF_CONFIG)) {
            isCanBeConsumed = true
            isCanBeResolved = false
            attributes { // Maybe needs change?
                attribute(Usage.USAGE_ATTRIBUTE, project.objects.named(Usage.JAVA_RUNTIME))
                attribute(Category.CATEGORY_ATTRIBUTE, project.objects.named(Category.LIBRARY))
                attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, project.objects.named(LibraryElements.JAR))
                attribute(Bundling.BUNDLING_ATTRIBUTE, project.objects.named(Bundling.EXTERNAL))
                attribute(Obfuscation.OBFUSCATION_ATTRIBUTE, project.objects.named(Obfuscation.OBFUSCATED))
            }
            outgoing.artifact(reobfJar.outputJar)
        }
    }
}
