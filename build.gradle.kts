import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder
import java.net.URL

val minixVersion: String by project
val version: String by project

plugins {
    id("dev.racci.minix.kotlin")
    id("dev.racci.minix.copyjar")
    id("dev.racci.minix.purpurmc")
    id("dev.racci.minix.nms")
    id("org.jetbrains.dokka")
    kotlin("plugin.atomicfu") version "1.6.20-RC2"
    kotlin("plugin.serialization")
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(project("Minix-Core"))
    implementation(project("Minix-API"))
    implementation(libs.minecraft.bstats)
    implementation(libs.logging.sentry)
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
    libraries = listOf(
        libs.kotlin.stdlib.get().toString(),
        libs.kotlin.reflect.get().toString(),
        libs.kotlinx.dateTime.get().toString(),
        libs.kotlinx.coroutines.get().toString(),
        libs.kotlinx.immutableCollections.get().toString(),
        libs.kotlinx.serialization.json.get().toString(),
        libs.exposed.core.get().toString(),
        libs.exposed.dao.get().toString(),
        libs.exposed.jdbc.get().toString(),
        libs.exposed.dateTime.get().toString(),
        libs.hikariCP.get().toString(),
        libs.koin.core.get().toString(),
        libs.mordant.get().toString(),
        libs.caffeine.get().toString(),
        libs.kotlinx.serialization.kaml.get().toString(),
        "org.jetbrains.kotlinx:atomicfu-jvm:0.17.1",
        libs.cloud.minecraft.paper.get().toString(),
        libs.cloud.kotlin.coroutines.get().toString(),
        libs.cloud.kotlin.extensions.get().toString(),
        libs.cloud.minecraft.extras.get().toString(),
        libs.adventure.kotlin.get().toString(),
        libs.ktor.client.core.get().toString(),
        libs.ktor.client.cio.get().toString(),
        "net.kyori:adventure-serializer-configurate4:4.10.1",
        "org.spongepowered:configurate-hocon:4.1.2",
        "org.spongepowered:configurate-extra-kotlin:4.1.2",
        "org.reflections:reflections:0.10.2"
    )
    website = "https://github.com/DaRacci/Minix"
}

allprojects {

    apply(plugin = "dev.racci.minix.kotlin")
    apply(plugin = "dev.racci.minix.purpurmc")
    apply(plugin = "dev.racci.minix.nms")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "org.jetbrains.dokka")

    dependencies {
        testImplementation(rootProject.libs.bundles.kotlin)
        testImplementation(rootProject.libs.bundles.kotlinx)
        testImplementation(rootProject.libs.testing.strikt)
        testImplementation(rootProject.libs.testing.junit5)
        testImplementation(rootProject.libs.koin.test) { exclude("org.jetbrains.kotlin") }
        testImplementation("net.kyori:adventure-serializer-configurate4:4.10.1")
        testImplementation(kotlin("test"))
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
    dependencies {
        compileOnly(rootProject.libs.kotlin.stdlib)
        compileOnly(rootProject.libs.kotlin.reflect)
        compileOnly(rootProject.libs.kotlinx.dateTime)
        compileOnly(rootProject.libs.kotlinx.immutableCollections)
        compileOnly(rootProject.libs.kotlinx.serialization.json)
        compileOnly(rootProject.libs.kotlinx.coroutines)
        compileOnly(rootProject.libs.koin.core)
        compileOnly(rootProject.libs.caffeine)
        compileOnly("org.jetbrains.kotlinx:atomicfu:0.17.1")
        compileOnly(rootProject.libs.cloud.minecraft.paper)
        compileOnly(rootProject.libs.bundles.cloud.kotlin)
        compileOnly(rootProject.libs.cloud.minecraft.extras)
        compileOnly(rootProject.libs.adventure.kotlin)
        compileOnly("net.kyori:adventure-serializer-configurate4:4.10.1")
        compileOnly("org.spongepowered:configurate-hocon:4.1.2")
        compileOnly("org.spongepowered:configurate-extra-kotlin:4.1.2")
        compileOnly("org.bstats:bstats-bukkit:3.0.0")
    }
}

fun included(
    build: String,
    task: String
) = gradle.includedBuild(build).task(task)

tasks {

    shadowJar {
        val location = "dev.racci.minix.libs"
        relocate("org.bstats", "$location.bstats")
        relocate("io.sentry", "$location.sentry")
    }

    ktlintFormat {
        dependsOn(gradle.includedBuilds.map { it.task(":ktlintFormat") })
    }

    build {
        dependsOn(gradle.includedBuilds.map { it.task(":build") })
    }

    clean {
        dependsOn(gradle.includedBuilds.map { it.task(":clean") })
    }

    withType<org.jetbrains.dokka.gradle.DokkaMultiModuleTask> {
        outputDirectory.set(File("$rootDir/docs"))
    }
}
