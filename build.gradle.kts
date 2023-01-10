import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import dev.racci.minix.gradle.data.MCTarget
import dev.racci.minix.gradle.ex.shadowJar
import dev.racci.minix.gradle.ex.whenEvaluated
import dev.racci.minix.gradle.ex.withMCTarget
import dev.racci.minix.gradle.extensions.MinixPublishingExtension.PublicationSpec.Version
import dev.racci.paperweight.mpp.paperweightDevBundle
import dev.racci.paperweight.mpp.reobfJar
import dev.racci.slimjar.extensions.slimApi
import dev.racci.slimjar.extensions.slimJar
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import io.papermc.paperweight.util.registering
import kotlinx.kover.KoverPlugin
import kotlinx.validation.KotlinApiCompareTask
import net.minecrell.pluginyml.bukkit.BukkitPlugin
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import java.io.ByteArrayOutputStream

val relocatePrefix = "dev.racci.minix.libs"

// Workaround for (https://youtrack.jetbrains.com/issue/KTIJ-19369)
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.mpp)
    id(libs.plugins.minix.get().pluginId) version "0.3.4"
    id("dev.racci.paperweight.mpp")
    alias(libs.plugins.shadow)
    id("dev.racci.slimjar") version "2.0.0-SNAPSHOT"
    id("org.jetbrains.kotlinx.kover") version "0.6.1" // TODO: Catalog and convention
    id("io.gitlab.arturbosch.detekt") version "1.22.0"

    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.plugin.dokka)
    alias(libs.plugins.kotlin.plugin.atomicfu) apply false
    alias(libs.plugins.kotlin.plugin.serialization) apply false
    alias(libs.plugins.kotlin.plugin.binaryValidator)

    alias(libs.plugins.minecraft.pluginYML) // TODO: MPP
    alias(libs.plugins.minecraft.runPaper)
    alias(libs.plugins.minecraft.minotaur)
}

koverMerged {
    enable()
    filters {
        classes {
            includes += "dev.racci.minix"
        }
    }
    htmlReport {
        onCheck.set(false)
        reportDir.set(layout.buildDirectory.dir("reports/kover/html"))
    }
}

apiValidation {
    ignoredProjects = subprojects.filter { it.name.contains("core") }.map(Project::getName).toMutableSet()

    ignoredPackages.add("dev.racci.minix.core")
    nonPublicMarkers.add("dev.racci.minix.api.MinixInternal")
}

minix {
    minecraft {
        fun target(
            project: String,
            platform: MCTarget.Platform = MCTarget.Platform.PAPER
        ) = withMCTarget(project(":module-$project"), platform, applyMinix = false)
        target("integrations")
        target("jumper")
        target("data", MCTarget.Platform.PURPUR)
    }
}

runPaper {
    disablePluginJarDetection()
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.racci.dev/releases/")
    maven("https://repo.racci.dev/snapshots/")

    maven("https://repo.papermc.io/maven-public/") {
        mavenContent {
            includeGroupByRegex("io.papermc([.a-z]+)?")
        }
    }

    maven("https://repo.purpurmc.org/snapshots") {
        mavenContent {
            includeGroup("org.purpurmc.purpur")
        }
    }

    maven("https://repo.fvdh.dev/releases") {
        mavenContent {
            releasesOnly()
            includeGroup("net.frankheijden.serverutils")
        }
    }

    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") {
        mavenContent {
            releasesOnly()
            includeModule("me.clip", "placeholderapi")
        }
    }
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.22.0")
}

fun Project.emptySources() = project.sourceSets.none { set -> set.allSource.any { file -> file.extension == "kt" } }

fun SourceDirectorySet.maybeExtend(vararg objects: Any) {
    objects.map {
        when (it) {
            is File -> it
            is String -> project.file(it)
            else -> error("Unknown type: $it")
        }
    }.filter { it.exists() }.also { srcDirs(it) }
}

inline fun <reified T : Task> TaskContainer.apiTask(
    prefix: String?,
    suffix: String,
    configure: T.() -> Unit = {}
): T = buildString {
    if (prefix != null) append(prefix)
    if (prefix != null) append("Api") else append("api")
    append(suffix)
}.let<String, T>(::getByName).also(configure)

fun Project.maybeConfigureBinaryValidator(prefix: String? = null) {
    this.tasks {
        // For some reason this task likes to delete the entire folder contents,
        // So we need all projects to have their own sub folder.
        val name = if (prefix != null) "plugin-$prefix" else project.name
        val apiDir = file("$rootDir/config/api/${name.toLowerCase()}")
        apiTask<Sync>(prefix, "Dump") { destinationDir = apiDir }
        apiTask<KotlinApiCompareTask>(prefix, "Check") { projectApiDir = apiDir }
    }
}

kotlin {
    fun KotlinSourceSet.setDirs(module: String, api: Boolean) {
        kotlin.srcDirs.clear()
        val prefix = buildString {
            append("minix-plugin/")
            if (api) append("api") else append("core")
            append('-').append(module)
            append("/src/main")
        }.let(::file)

        val generated = buildDir.resolve("generated/ksp/$module/$name/")

        kotlin.maybeExtend(
            prefix.resolve("kotlin"),
            prefix.resolve("java"),
            generated
        )

        resources.maybeExtend(prefix.resolve("resources"))
    }

    // Koin generated sources aren't complicit.
    explicitApiWarning()

    sourceSets {
        val commonAPI by creating {
            setDirs("common", true)

            dependencies {
                slimApi(libs.kotlin.stdlib)
                slimApi(libs.kotlin.reflect)
                slimApi(libs.kotlinx.dateTime)
                slimApi(libs.kotlinx.atomicfu)
                slimApi(libs.kotlinx.coroutines)
                slimApi(libs.kotlinx.immutableCollections)

                slimApi(libs.arrow.core)
                slimApi(libs.arrow.optics)
                slimApi(libs.arrow.fx.coroutines)
                slimApi(libs.arrow.optics.reflect)
                slimApi(libs.arrow.laws)
            }
        }

        val commonMain by getting {
            setDirs("common", false)
            dependsOn(commonAPI)

            dependencies {
                // Workaround for transitive dependencies not being taken from project deps for some reason.
                fun withDependencies(module: String) = project(":module-$module").dependencyProject.whenEvaluated {
                    slimApi(this)
                    configurations["compileOnlyApi"].dependencies.forEach(::slimApi)
                }

                withDependencies("autoscanner")
                withDependencies("common")
                withDependencies("data")
                withDependencies("flowbus")
                withDependencies("integrations")
                withDependencies("ticker")

                slimApi(libs.koin.core)
                slimApi(libs.mordant)
                slimApi(libs.caffeine)
                slimApi(libs.aedile)
                slimApi(libs.minecraft.bstats.base)
                slimApi(libs.toolfactory)
                slimApi(libs.slimjar)
                slimApi(libs.mariadb)

                slimApi(libs.sentry.core)
                slimApi(libs.sentry.kotlin)

                slimApi(libs.hikariCP)
                slimApi(libs.adventure.configurate)

                // Annotations
                compileOnly(libs.apiguardian)
                compileOnly(libs.koin.annotations)
            }
        }

        val paperAPI by creating {
            setDirs("paper", true)
            dependsOn(commonAPI)

            dependencies {
                compileOnly(libs.minecraft.api.landsAPI)
                compileOnly(libs.minecraft.api.placeholderAPI)
            }
        }

        val paperMain by creating {
            setDirs("paper", false)
            resources.srcDir(tasks.generateBukkitPluginDescription.get())
            dependsOn(commonMain)
            dependsOn(paperAPI)

            dependencies {
                slimApi(project(":module-jumper"))

                // Integrations
                compileOnly(libs.minecraft.api.serverutils)

                // MinixLogger backend hooks for paper logger
                compileOnly("org.apache.logging.log4j:log4j-core:2.19.0")
                compileOnly("net.minecrell:terminalconsoleappender:1.3.0")
                compileOnly("org.jline:jline-terminal-jansi:3.21.0")

                slimApi(libs.minecraft.bstats.bukkit)
                slimApi(libs.configurate.hocon)
                slimApi(libs.configurate.extra.kotlin)

                withMCTarget(MCTarget.Platform.PAPER, "1.19.3", applyMinix = false)
                paperweightDevBundle("org.purpurmc.purpur", libs.versions.minecraft.get())
            }
        }

        targets {
            jvm("paper") {
                slimJar {
                    relocate("io.sentry", "$relocatePrefix.sentry")
                    relocate("org.bstats", "$relocatePrefix.bstats")
                }

                shadowJar {
                    dependencyFilter.include { dep ->
                        dep.moduleGroup.startsWith("dev.racci") ||
                            dep.moduleGroup.startsWith("io.github.slimjar")
                    }
                }

                project.maybeConfigureBinaryValidator(this.name)
                project.apply<BukkitPlugin>()
                project.bukkit {
                    name = "Minix"
                    prefix = "Minix"
                    author = "Racci"
                    apiVersion = "1.19"
                    version = rootProject.version.toString()
                    main = "dev.racci.minix.jumper.MinixInit"
                    load = PluginLoadOrder.STARTUP
                    loadBefore = listOf("eco")
                    softDepend = listOf("PlaceholderAPI", "Lands", "ServerUtils")
                    website = "https://github.com/DaRacci/Minix"
                }
            }
        }
    }
}.also { // Order matters
    dependencies {
        add("kspPaper", libs.koin.ksp)
    }
}

modrinth {
    val versionSpec = Version.parse(version.toString())

    token.set(properties["modrinth.token"].toString())
    projectId.set("OtoWQs96")
    versionNumber.set("${versionSpec.major}.${versionSpec.minor}.${versionSpec.patch}")
    syncBodyFrom.set(file("README.md").readText())
    changelog.set(
        project.provider {
            val output = ByteArrayOutputStream()
            exec {
                executable("/home/racci/.local/share/cargo/bin/cog")
                args("changelog", "--template", "config/modrinth", "--at", "HEAD")
                standardOutput = output
            }

            output.toString()
        }
    )
    versionType.set(
        when (versionSpec.snapshotType) {
            null -> "release"
            "alpha" -> "alpha"
            "beta", "rc", "snapshot" -> "beta"
            else -> error("Unknown snapshot type: ${versionSpec.snapshotType}")
        }
    )

    uploadFile.set(kotlin.targets["paper"].artifactsTaskName.let(project.tasks::named).get().outputs.files.singleFile)

    gameVersions.add(libs.versions.minecraft.get().substringBefore('-'))
    loaders.addAll("paper", "purpur")
}

val reportMerge by tasks.registering<ReportMergeTask> {
    output.set(buildDir.resolve("reports/detekt/detekt.sarif"))
}

tasks {
    val dependTask = named<Jar>(kotlin.targets["paper"].artifactsTaskName) // Cannot be called inside configure block.
    runServer {
        dependsOn(dependTask)
        minecraftVersion(libs.versions.minecraft.get().substringBefore('-'))
        pluginJars(dependTask.get().archiveFile)
    }

    withType<ShadowJar> {
        mergeServiceFiles()
        relocate("io.github.slimjar", "$relocatePrefix.slimjar")
        relocate("org.koin.ksp.generated", "$relocatePrefix.generated.koin")

        exclude {
            it.path.endsWith("Minix-paper-5.0.0-alpha-dev-all.jar")
        }
    }

    modrinth.get().dependsOn(modrinthSyncBody)
}

subprojects {
    apply<IdeaPlugin>()
    apply<KoverPlugin>()
    apply<DokkaPlugin>()
    apply<DetektPlugin>()
    apply<JavaLibraryPlugin>()
    apply<MavenPublishPlugin>()
    apply<KotlinPluginWrapper>()

    maybeConfigureBinaryValidator()

    dependencies {
        val common = project(":module-common")
        if (this@subprojects.project !== common.dependencyProject) {
            "compileOnly"(common)
            "testImplementation"(common)
        }

        @Suppress("UnstableApiUsage")
        "testImplementation"(rootProject.libs.testing.kotest.junit5)
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

allprojects {
    detekt {
        toolVersion = "1.22.0"
        config = rootProject.files("config/detekt/detekt.yml")
        buildUponDefaultConfig = true
        parallel = true
        basePath = rootProject.projectDir.path
    }

    tasks.withType<Detekt> {
        reports {
            sarif.required.set(true)
        }

        finalizedBy(reportMerge)
        reportMerge {
            input.from(sarifReportFile)
        }
    }

    idea.module {
        val generatedDir = buildDir.resolve("generated/ksp/main/kotlin")
        sourceDirs = sourceDirs + generatedDir
        generatedSourceDirs = generatedSourceDirs + generatedDir
    }
}
