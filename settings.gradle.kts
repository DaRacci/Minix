enableFeaturePreview("VERSION_CATALOGS")
pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.racci.dev/releases")
        maven("https://papermc.io/repo/repository/maven-public/")
    }
    plugins {
        val kotlinVersion: String by settings
        kotlin("plugin.serialization") version kotlinVersion
        id("org.jetbrains.dokka") version kotlinVersion
    }
    val minixConventions: String by settings
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.startsWith("dev.racci.minix")) {
                useVersion(minixConventions)
            }
        }
    }
}

dependencyResolutionManagement {
    repositories {
        maven("https://repo.racci.dev/releases")
    }

    versionCatalogs.create("libs") {
        val minixConventions: String by settings
        from("dev.racci:catalog:$minixConventions")
    }
}

rootProject.name = "Minix"

include("Minix-Core")
include("Minix-API")
