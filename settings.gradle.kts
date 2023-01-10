enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Minix"

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.racci.dev/releases")
        maven("https://repo.racci.dev/snapshots")
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://repo.racci.dev/releases/") {
            name = "Catalog Repository"
            mavenContent {
                releasesOnly()
                includeModule("dev.racci", "catalog")
            }
        }
    }

    versionCatalogs.create("libs") {
        val minixBuild: String by settings
        val kotlinVersion: String by settings
        val conventions = kotlinVersion.plus("-").plus(minixBuild)
        from("dev.racci:catalog:$conventions")
    }
}

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
