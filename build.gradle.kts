import Dev_racci_minix_platform_gradle.Deps
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
    library(Deps.kotlin.stdlib)
    library(Deps.kotlin.reflect)
    library(Deps.kotlinx.dateTime.plus(":0.3.2"))
    library(Deps.kotlinx.coroutines.plus(":1.6.0"))
    library(Deps.kotlinx.immutableCollections.plus("-jvm:0.3.5"))
    library(Deps.kotlinx.serialization.json.plus(":1.3.2"))
    library(Deps.exposed.core.plus(":0.37.3"))
    library(Deps.exposed.dao.plus(":0.37.3"))
    library(Deps.exposed.jdbc.plus(":0.37.3"))
    library(Deps.koin.core.plus("-jvm:3.1.5"))
    library(Deps.logging.sentry.plus(":6.0.0-alpha.2"))
    library(Deps.mordant.plus("-jvm:2.0.0-beta4"))
    implementation(Deps.adventure.kotlin)
    implementation(Deps.adventure.minimessage)
    implementation(Deps.racci.minixNMS.plus(":$minixVersion"))
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

    assemble {
        dependsOn(reobfJar)
    }

    shadowJar {
        dependencyFilter.exclude {
            it.moduleGroup == "org.jetbrains.kotlin" || it.moduleGroup == "org.jetbrains.intellij" || it.moduleGroup == "org.jetbrains" || it.moduleName == "adventure-api" || it.moduleName == "adventure-text-serializer-*" || it.moduleName == "adventure-key" || it.moduleName == "examination-*"
        }
        relocate("net.kyori.adventure.text.minimessage", "dev.racci.minix.libs.kyori.minimessage")
        relocate("net.kyori.adventure.text.extra.kotlin", "dev.racci.minix.libs.kyroi.kotlin")
    }

    compileKotlin {
        dependsOn(ktlintFormat)
        kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }
}

ktlint {
    enableExperimentalRules.set(false)
}
