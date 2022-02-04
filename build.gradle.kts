import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder

val minixConventions: String by project
val minixVersion: String by project
val kotlinVersion: String by project

plugins {
    id("dev.racci.minix.kotlin")
    id("dev.racci.minix.copyjar")
    id("dev.racci.minix.testing")
    id("dev.racci.minix.purpurmc")
    kotlin("plugin.serialization")
    id("dev.racci.minix.publication")
    id("dev.racci.minix.nms")
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    library(libs.kotlin.stdlib)
    library(libs.kotlin.reflect)
    library(libs.kotlinx.dateTime)
    library(libs.kotlinx.coroutines)
    library(libs.kotlinx.immutableCollections)
    library(libs.kotlinx.serialization.json)
    library(libs.exposed.core)
    library(libs.exposed.dao)
    library(libs.exposed.jdbc)
    library(libs.hikariCP)
    library(libs.koin.core)
    library(libs.logging.sentry)
    library(libs.mordant)
    library(libs.caffeine)
    implementation(libs.adventure.kotlin)
    implementation(libs.adventure.minimessage)
    implementation("dev.racci:Minix-NMS:$minixVersion")
}

bukkit {
    name = "Minix"
    prefix = "Minix"
    author = "Racci"
    apiVersion = "1.18"
    version = rootProject.version.toString()
    main = "dev.racci.minix.core.MinixImpl"
    load = PluginLoadOrder.STARTUP
    loadBefore = listOf(
        "Sylphia"
    )
    website = "https://minix.racci.dev/"
}

tasks {

    shadowJar {
        dependencyFilter.exclude {
            it.moduleGroup == "org.jetbrains.kotlin" || it.moduleGroup == "org.jetbrains.intellij" || it.moduleGroup == "org.jetbrains" || it.moduleName == "adventure-api" || it.moduleName == "adventure-text-serializer-*" || it.moduleName == "adventure-key" || it.moduleName == "examination-api" || it.moduleName == "examination-string"
        }
        relocate("net.kyori.adventure.text.minimessage", "dev.racci.minix.libs.kyori.minimessage")
        relocate("net.kyori.adventure.text.extra.kotlin", "dev.racci.minix.libs.kyroi.kotlin")
        relocate("dev.racci.minix.nms", "dev.racci.minix.libs.nms")
    }

    compileKotlin {
        kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }
}

ktlint {
    enableExperimentalRules.set(false)
}
