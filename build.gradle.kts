import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.google.devtools.ksp.gradle.KspGradleSubplugin
import dev.racci.minix.gradle.data.MCTarget
import dev.racci.minix.gradle.ex.withMCTarget
import dev.racci.paperweight.mpp.paperweightDevBundle
import dev.racci.slimjar.extensions.slimApi
import dev.racci.slimjar.extensions.slimJar
import kotlinx.kover.KoverPlugin
import kotlinx.validation.KotlinApiCompareTask
import net.minecrell.pluginyml.bukkit.BukkitPlugin
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin

val minixVersion: String by project
val version: String by project
val relocatePrefix = "dev.racci.minix.libs"

// Workaround for (https://youtrack.jetbrains.com/issue/KTIJ-19369)
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.mpp)
    id("dev.racci.minix") version "0.2.2"
    id("dev.racci.paperweight.mpp")
    alias(libs.plugins.shadow)
    alias(libs.plugins.kotlin.plugin.ktlint)
    id("dev.racci.slimjar") version "2.0.0-SNAPSHOT"
    id("org.jetbrains.kotlinx.kover") version "0.6.1" // TODO: Catalog and convention

    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.plugin.dokka)
    alias(libs.plugins.kotlin.plugin.atomicfu) apply false
    alias(libs.plugins.kotlin.plugin.serialization) apply false
    alias(libs.plugins.kotlin.plugin.binaryValidator)

    alias(libs.plugins.minecraft.pluginYML) // TODO: MPP
    alias(libs.plugins.minecraft.runPaper)
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
        // withMCTarget()
    }
}

runPaper.disablePluginJarDetection()

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

tasks {
//    runServer {
//        val dependTask = kotlin.targets["paper"].reobfJar
//        dependsOn(dependTask)
//        minecraftVersion("1.19.2")
//        pluginJars(dependTask.get().outputJar)
//    }
}

allprojects {
    configurations.whenObjectAdded {
        if (this.name == "testImplementation") {
            exclude("org.jetbrains.kotlin", "kotlin-test-junit")
        }

        // These refuse to resolve sometimes
        exclude("me.carleslc.Simple-YAML", "Simple-Configuration")
        exclude("me.carleslc.Simple-YAML", "Simple-Yaml")
        exclude("com.github.technove", "Flare")
    }

    afterEvaluate {

        // TODO: Disable unwanted modules
        kotlinExtension.apply {
            explicitApiWarning() // Koin generated sources aren't complicit.
            this.kotlinDaemonJvmArgs = listOf("-Xemit-jvm-type-annotations")

            sourceSets.forEach { set -> set.kotlin.maybeExtend("$buildDir/generated/ksp/main/kotlin") }
            (sourceSets.findByName("test") ?: sourceSets.getByName("commonTest")).apply { kotlin.maybeExtend("$buildDir/generated/ksp/main/kotlin") }
        }

        if (project.emptySources()) {
            tasks.apiDump.get().enabled = false
            tasks.apiCheck.get().enabled = false
        } else {
            apply<KtlintPlugin>()
            configure<KtlintExtension> { baseline.set(file("$rootDir/config/ktlint/baseline-${project.name}.xml")) }
            project.maybeConfigureBinaryValidator()
        }
    }

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://repo.racci.dev/releases/") {
            mavenContent {
                releasesOnly()
                includeGroupByRegex("dev.racci(\\.[a-z]+)?")
            }
        }

        maven("https://repo.racci.dev/snapshots/")

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
        maven("https://jitpack.io")
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.purpurmc.org/snapshots")
    }

    tasks {
        withType<Test>().configureEach {
            enabled = false
            useJUnitPlatform()
        }
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

        kotlin.maybeExtend(
            prefix.resolve("kotlin"),
            prefix.resolve("java")
        )

        resources.maybeExtend(prefix.resolve("resources"))
    }

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
                slimApi(project(":module-autoscanner"))
                slimApi(project(":module-common"))
                slimApi(project(":module-data"))
                slimApi(project(":module-flowbus"))
                slimApi(project(":module-integrations"))
                slimApi(project(":module-ticker"))

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

                withMCTarget(MCTarget.Platform.PAPER, "1.19.3", applyMinix = false)
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
                paperweightDevBundle("org.purpurmc.purpur", project.properties["serverVersion"] as String)
            }
        }

        targets {
            jvm("paper") {
                slimJar {
                    relocate("io.sentry", "$relocatePrefix.sentry")
                    relocate("org.bstats", "$relocatePrefix.bstats")
                }

                project.maybeConfigureBinaryValidator(this.name)
                project.apply<BukkitPlugin>()
                project.bukkit {
                    name = "Minix"
                    prefix = "Minix"
                    author = "Racci"
                    apiVersion = "1.19"
                    version = rootProject.version.toString()
                    main = "dev.racci.minix.core.MinixInit"
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
        kspCommonMainMetadata(libs.koin.ksp)
        add("kspPaper", libs.koin.ksp)
    }
}

subprojects {
    apply<DokkaPlugin>()
    apply<JavaLibraryPlugin>()
    apply<MavenPublishPlugin>()
    apply<KotlinPlatformJvmPlugin>()
    apply<KspGradleSubplugin>()
    apply<KoverPlugin>()

    dependencies {
        val common = project(":module-common")
        if (this@subprojects.project !== common.dependencyProject) {
            "compileOnly"(common)
            "testImplementation"(common)
        }

        ksp(rootProject.libs.koin.ksp)
        ksp(rootProject.libs.arrow.optics.ksp)
    }

//    tasks {
//
//        dokkaHtml.get().dokkaSourceSets.configureEach {
//            includeNonPublic.set(false)
//            skipEmptyPackages.set(true)
//            reportUndocumented.set(true)
//            displayName.set(project.name.split("-")[1])
//            platform.set(Platform.jvm)
//            sourceLink {
//                localDirectory.set(file("src/main/kotlin"))
//                remoteUrl.set(URL("https://github.com/DaRacci/Minix/blob/master/src/main/kotlin"))
//                remoteLineSuffix.set("#L")
//            }
//            jdkVersion.set(17)
//            externalDocumentationLink {
//                url.set(URL("https://minix.racci.dev/"))
//            }
//        }
//    }
}

tasks.withType<ShadowJar> {
    relocate("io.github.slimjar", "$relocatePrefix.slimjar")
    relocate("org.koin.ksp.generated", "$relocatePrefix.generated.koin")
}
