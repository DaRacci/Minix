@file:Suppress("UnstableApiUsage")

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.racci.dev/releases")
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

dependencyResolutionManagement {
    repositories {
        maven("https://repo.racci.dev/releases")
        maven("https://repo.racci.dev/snapshots")
    }

    versionCatalogs.create("libs") {
        val minixVersion: String by settings
        val kotlinVersion: String by settings
        val conventions = kotlinVersion.plus("-").plus(minixVersion.substringAfterLast('.'))

        from("dev.racci:catalog:$conventions")
    }
}

rootProject.name = "Minix"

includeBuild("paperweight-mpp")

sequenceOf(
    "common",
    "autoscanner",
    "data",
    "jumper",
//    "wrappers",
    "flowbus",
    "integrations",
    "ticker"
//    "updater",
).forEach { proj ->
    val path = "module-$proj"
    include(":$path")
    project(":$path").projectDir = file("minix-modules/$path")
}
