import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder
import java.net.URL

val minixVersion: String by project
val version: String by project

plugins {
    id("dev.racci.minix.kotlin")
    id("dev.racci.minix.copyjar")
    id("dev.racci.minix.purpurmc")
    id("dev.racci.minix.nms")
    id("org.jetbrains.dokka") version "1.6.20"
    kotlin("plugin.atomicfu") version "1.6.20-RC2"
    kotlin("plugin.serialization")
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project("Minix-Core"))
    implementation(project("Minix-API"))
}

kotlin {
    kotlinDaemonJvmArgs = listOf("-Xemit-jvm-type-annotations")
}

bukkit {
    name = "Minix"
    prefix = "Minix"
    author = "Racci"
    apiVersion = "1.18"
    version = rootProject.version.toString()
    main = "dev.racci.minix.core.MinixImpl"
    load = PluginLoadOrder.STARTUP
    loadBefore = listOf("Terix")
    website = "https://github.com/DaRacci/Minix"
}

allprojects {

    apply(plugin = "dev.racci.minix.kotlin")
    apply(plugin = "dev.racci.minix.purpurmc")
    apply(plugin = "dev.racci.minix.nms")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "org.jetbrains.dokka")

    dependencies {
        testImplementation(rootProject.libs.bundles.testing) { exclude("org.jetbrains.kotlin") }
        testImplementation(rootProject.libs.bundles.kotlin)
        testImplementation(rootProject.libs.bundles.kotlinx)
    }

    tasks {

        test.get().useJUnitPlatform()

        dokkaHtml.get().dokkaSourceSets.configureEach {
            includeNonPublic.set(false)
            skipEmptyPackages.set(true)
            displayName.set(project.name.split("-")[1])
            platform.set(org.jetbrains.dokka.Platform.jvm)
            sourceLink {
                localDirectory.set(file("src/main/kotlin"))
                remoteUrl.set(URL("https://github.com/DaRacci/Minix/blob/master/src/main/kotlin"))
                remoteLineSuffix.set("#L")
            }
            jdkVersion.set(17)
            externalDocumentationLink {
                url.set(URL("https://minix.racci.dev/"))
            }
        }
    }
}

subprojects {
    apply(plugin = "maven-publish")
}

fun included(
    build: String,
    task: String
) = gradle.includedBuild(build).task(task)

inline fun <reified T : Task> TaskProvider<T>.alsoSubprojects(crossinline block: T.() -> Unit = {}) {
    this {
        dependsOn(gradle.includedBuilds.map { it.task(":$name") })
        block()
    }
}

tasks {

    shadowJar {
        val location = "dev.racci.minix.libs"
        this.dependencyFilter.include {
            it.moduleGroup == "org.bstats" ||
                it.moduleGroup == "io.sentry" ||
                it.moduleName == "Minix-Core" ||
                it.moduleName == "Minix-API"
        }
        relocate("org.bstats", "$location.bstats")
        relocate("io.sentry", "$location.sentry")
    }

    ktlintFormat.alsoSubprojects()
    build.alsoSubprojects()
    clean.alsoSubprojects()

    withType<org.jetbrains.dokka.gradle.DokkaMultiModuleTask> {
        outputDirectory.set(File("$rootDir/docs"))
    }
}
