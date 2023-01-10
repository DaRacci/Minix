enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.racci.dev/releases")
        maven("https://papermc.io/repo/repository/maven-public/")
    }

    plugins {
        val kotlinVersion: String by settings

        kotlin("plugin.serialization") version kotlinVersion
        kotlin("plugin.atomicfu") version kotlinVersion
        id("org.jetbrains.dokka") version kotlinVersion

        id("dev.racci.slimjar") version "1.4.1"
        id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
        id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.11.1"
    }

    plugins {
        val kotlinVersion: String by settings
        kotlin("plugin.serialization") version kotlinVersion
        id("org.jetbrains.dokka") version kotlinVersion
    }

    val minixVersion: String by settings
    val kotlinVersion: String by settings
    val conventions = kotlinVersion.plus("-").plus(minixVersion.substringAfterLast('.'))
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.startsWith("dev.racci.minix")) {
                useVersion(conventions)
            }
        }
    }
}

dependencyResolutionManagement {
    repositories {
        maven("https://repo.racci.dev/releases")
    }

    versionCatalogs.create("libs") {
        val minixVersion: String by settings
        val kotlinVersion: String by settings
        val conventions = kotlinVersion.plus("-").plus(minixVersion.substringAfterLast('.'))

        from("dev.racci:catalog:$conventions")
    }
}

rootProject.name = "Minix"

include(":Minix-API")
include(":Minix-Core")

include(":minix-core")
include(":minix-core:core-integrations")
include(":minix-core:core-updater")
