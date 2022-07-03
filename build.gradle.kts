import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder
import java.net.URL

val minixVersion: String by project
val version: String by project

plugins {
    id("dev.racci.minix")
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
    apiVersion = "1.19"
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
        testImplementation(platform(kotlin("bom")))
        testImplementation(rootProject.libs.bundles.kotlin)
        testImplementation(rootProject.libs.bundles.kotlinx)
        testImplementation(rootProject.libs.bundles.testing)
        testImplementation(rootProject.libs.minecraft.bstats)
        testImplementation("io.insert-koin:koin-test:3.+")
        testImplementation("io.insert-koin:koin-test-junit5:3.+")
        testImplementation("io.mockk:mockk:1.12.4")
        testImplementation(project(":Minix-API"))
    }

    configurations {
        testImplementation.get().exclude("org.jetbrains.kotlin", "kotlin-test-junit")
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
        dependencyFilter.include {
            it.moduleGroup == "io.ktor" ||
                it.moduleGroup == "io.sentry" ||
                it.moduleGroup == "org.bstats" ||
                it.moduleGroup == "io.insert-koin" ||
                it.moduleGroup == "org.jetbrains.kotlin" ||
                it.moduleGroup == "org.jetbrains.kotlinx" ||
                it.moduleGroup == "org.jetbrains.exposed" ||
                it.moduleName == "configurate-extra-kotlin" ||
                it.moduleName == "cloud-kotlin-extensions" ||
                it.moduleName == "cloud-kotlin-coroutines" ||
                it.moduleName == "adventure-extra-kotlin" ||
                it.moduleName == "Minix-Core" ||
                it.moduleName == "Minix-API" ||
                it.moduleName == "caffeine"
        }
    }

    ktlintFormat.alsoSubprojects()
    build.alsoSubprojects()
    clean.alsoSubprojects()

    withType<org.jetbrains.dokka.gradle.DokkaMultiModuleTask> {
        outputDirectory.set(File("$rootDir/docs"))
    }
}
