@file:Suppress("UnstableApiUsage")

import java.util.Properties

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "paperweight-mpp"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.racci.dev/releases")
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.racci.dev/releases")
        maven("https://papermc.io/repo/repository/maven-public/")
    }

    versionCatalogs.create("libs") {
        val properties = Properties().apply { load(rootDir.toPath().resolveSibling(Project.GRADLE_PROPERTIES).toFile().inputStream()) }

        val minixVersion: String by properties
        val kotlinVersion: String by properties
        val conventions = kotlinVersion.plus("-").plus(minixVersion.substringAfterLast('.'))

        from("dev.racci:catalog:$conventions")
    }
}
