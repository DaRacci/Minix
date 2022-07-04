import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder
import java.net.URL

val minixVersion: String by project
val version: String by project

plugins {
//    id("dev.racci.minix")
    id("dev.racci.minix.kotlin")
    id("dev.racci.minix.copyjar")
    id("dev.racci.minix.purpurmc")
    id("dev.racci.minix.nms")
    id("org.jetbrains.dokka") version "1.6.20"
    kotlin("plugin.atomicfu") version "1.6.20-RC2"
    kotlin("plugin.serialization")
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
    id("io.github.slimjar") version "1.3.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project("Minix-Core"))
    implementation(project("Minix-API"))
    implementation("io.github.slimjar:slimjar:1.2.7")
    // Has to be shaded
    slim(libs.minecraft.bstats)
    // We Shade these two due to the puffer fish conflict
    slim(libs.sentry.core)
    slim(libs.sentry.kotlin)
    // Shade these due to conflict with eco
    slim(libs.bundles.kotlin)
    slim(libs.bundles.kotlinx)
    slim(libs.kotlinx.serialization.json)
    slim(libs.bundles.exposed)
    slim(libs.caffeine)
    // Because of the kotlin shade
    slim(libs.adventure.kotlin)
    slim(libs.koin.core)
    slim(libs.ktor.client.core)
    slim(libs.ktor.client.cio)
    slim(libs.cloud.kotlin.extensions)
    slim(libs.cloud.kotlin.coroutines)
    slim(libs.configurate.extra.kotlin)

    slim(rootProject.libs.adventure.configurate)

    slim(rootProject.libs.cloud.core)
    slim(rootProject.libs.cloud.minecraft.paper)
    slim(rootProject.libs.cloud.minecraft.extras)

    slim(rootProject.libs.mordant)

    slim("io.github.classgraph:classgraph:4.8.146")
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
    main = "dev.racci.minix.core.MinixInit"
    load = PluginLoadOrder.STARTUP
    loadBefore = listOf("eco")
    website = "https://github.com/DaRacci/Minix"
}

allprojects {

    apply(plugin = "dev.racci.minix.kotlin")
    apply(plugin = "dev.racci.minix.purpurmc")
    apply(plugin = "dev.racci.minix.nms")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "io.github.slimjar")

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
            it.moduleGroup == "io.sentry" ||
                it.moduleGroup == "org.bstats" ||
                it.moduleGroup == "io.github.slimjar" ||
                it.moduleName == "Minix-Core" ||
                it.moduleName == "Minix-API"
        }
        val prefix = "dev.racci.minix.libs"
        relocate("io.sentry", "$prefix.io.sentry")
        relocate("org.bstats", "$prefix.org.bstats")
        relocate("io.github.slimjar", "$prefix.io.github.slimjar")
    }

    ktlintFormat.alsoSubprojects()
    build.alsoSubprojects()
    clean.alsoSubprojects()

    withType<org.jetbrains.dokka.gradle.DokkaMultiModuleTask> {
        outputDirectory.set(File("$rootDir/docs"))
    }
}
