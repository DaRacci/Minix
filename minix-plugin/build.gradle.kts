import dev.racci.minix.gradle.data.MCTarget
import dev.racci.minix.gradle.ex.shadowJar
import dev.racci.minix.gradle.ex.whenEvaluated
import dev.racci.minix.gradle.ex.withMCTarget
import dev.racci.slimjar.extensions.slimApi
import dev.racci.slimjar.extensions.slimJar
import kotlinx.validation.KotlinApiBuildTask
import kotlinx.validation.KotlinApiCompareTask
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

val relocatePrefix = "dev.racci.minix.libs"

plugins {
    alias(libs.plugins.kotlin.mpp)
    alias(libs.plugins.shadow)
    alias(libs.plugins.ksp)

    id(libs.plugins.minix.get().pluginId)
    id(libs.plugins.slimjar.get().pluginId) version "2.0.0-SNAPSHOT"
//    id("dev.racci.paperweight.mpp")

    // TODO: MPP Support for these plugins
//    alias(libs.plugins.minecraft.pluginYML) // TODO: MPP
//    alias(libs.plugins.minecraft.runPaper)
//    alias(libs.plugins.minecraft.minotaur)
}

// runPaper {
//    disablePluginJarDetection()
// }

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

fun Project.maybeConfigureBinaryValidator(prefix: String? = null) {
    fun <T : Task> TaskContainer.apiTask(
        prefix: String?,
        suffix: String,
        configure: T.() -> Unit = {}
    ): T = buildString {
        if (prefix != null) {
            append(prefix)
            append("Api")
        } else {
            append("api")
        }
        append(suffix)
    }.let { (getByName(it) as T).also(configure) }

    tasks {
        // For some reason this task likes to delete the entire folder contents,
        // So we need all projects to have their own sub folder.
        val name = if (prefix != null) "plugin-$prefix" else project.name
        val subDir = "api/${name.toLowerCase()}"
        val apiDir = rootProject.layout.projectDirectory.file("config/$subDir").asFile
        apiTask<KotlinApiBuildTask>(prefix, "Build") {
            outputApiDir = rootProject.buildDir.resolve(
                "api/$subDir"
            )
        }
        apiTask<Sync>(prefix, "Dump") { destinationDir = apiDir }
        apiTask<KotlinApiCompareTask>(prefix, "Check") { projectApiDir = apiDir }
    }
}

// Workaround for transitive dependencies not being taken from project deps for some reason.
fun KotlinDependencyHandler.withDependencies(module: String) = project(
    ":minix-modules:module-$module"
).dependencyProject.whenEvaluated {
    slimApi(this)
    configurations["compileOnlyApi"].dependencies.forEach(::slimApi)
}

kotlin {
    // Koin generated sources aren't complicit.
    explicitApiWarning()

    sourceSets {
        val commonApi by creating {
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
            dependsOn(commonApi)

            dependencies {
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

        val paperApi by creating {
            dependsOn(commonApi)

            dependencies {
                compileOnly(libs.minecraft.api.landsAPI)
                compileOnly(libs.minecraft.api.placeholderAPI)
            }
        }

        create("paperMain") {
//            resources.srcDir(tasks.generateBukkitPluginDescription.get())
            dependsOn(commonMain)
            dependsOn(paperApi)

            dependencies {
                withDependencies("jumper")

                // Integrations
                compileOnly(libs.minecraft.api.serverutils)

                // MinixLogger backend hooks for paper logger
                compileOnly("org.apache.logging.log4j:log4j-core:2.19.0")
                compileOnly("net.minecrell:terminalconsoleappender:1.3.0")
                compileOnly("org.jline:jline-terminal-jansi:3.21.0")

                slimApi(libs.minecraft.bstats.bukkit)
                slimApi(libs.configurate.hocon)
                slimApi(libs.configurate.extra.kotlin)

                withMCTarget(MCTarget.Platform.PAPER, libs.versions.minecraft.get(), applyMinix = false)
//                paperweightDevBundle("org.purpurmc.purpur", libs.versions.minecraft.get())
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
//                project.apply<BukkitPlugin>()
//                project.bukkit {
//                    name = "Minix"
//                    prefix = "Minix"
//                    author = "Racci"
//                    apiVersion = "1.19"
//                    version = rootProject.version.toString()
//                    main = "dev.racci.minix.jumper.MinixInit"
//                    load = PluginLoadOrder.STARTUP
//                    loadBefore = listOf("eco")
//                    softDepend = listOf("PlaceholderAPI", "Lands", "ServerUtils")
//                    website = "https://github.com/DaRacci/Minix"
//                }
            }
        }
    }
}.also { // Order matters
    dependencies {
        add("kspPaper", libs.koin.ksp)
    }
}

// //modrinth {
// //    val versionSpec = Version.parse(version.toString())
// //
// //    token.set(properties["modrinth.token"].toString())
// //    projectId.set("OtoWQs96")
// //    versionNumber.set("${versionSpec.major}.${versionSpec.minor}.${versionSpec.patch}")
// //    syncBodyFrom.set(file("README.md").readText())
// //    changelog.set(
// //        project.provider {
// //            val output = ByteArrayOutputStream()
// //            exec {
// //                executable("/home/racci/.local/share/cargo/bin/cog")
// //                args("changelog", "--template", "config/modrinth", "--at", "HEAD")
// //                standardOutput = output
// //            }
// //
// //            output.toString()
// //        }
// //    )
// //    versionType.set(
// //        when (versionSpec.snapshotType) {
// //            null -> "release"
// //            "alpha" -> "alpha"
// //            "beta", "rc", "snapshot" -> "beta"
// //            else -> error("Unknown snapshot type: ${versionSpec.snapshotType}")
// //        }
// //    )
// //
// //    uploadFile.set(kotlin.targets["paper"].artifactsTaskName.let(project.tasks::named).get().outputs.files.singleFile)
// //
// //    gameVersions.add(libs.versions.minecraft.get().substringBefore('-'))
// //    loaders.addAll("paper", "purpur")
// //}
//
// tasks {
//    val dependTask = named<Jar>(kotlin.targets["paper"].artifactsTaskName) // Cannot be called inside configure block.
//    runServer {
//        dependsOn(dependTask)
//        minecraftVersion(libs.versions.minecraft.get().substringBefore('-'))
//        pluginJars(dependTask.get().archiveFile)
//    }
//
//    withType<ShadowJar> {
//        mergeServiceFiles()
//        relocate("io.github.slimjar", "$relocatePrefix.slimjar")
//        relocate("org.koin.ksp.generated", "$relocatePrefix.generated.koin")
//
//        exclude {
//            it.path.endsWith("Minix-paper-5.0.0-alpha-dev-all.jar")
//        }
//    }
//
//    modrinth.get().dependsOn(modrinthSyncBody)
// }
