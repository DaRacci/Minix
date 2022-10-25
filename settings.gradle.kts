enableFeaturePreview("VERSION_CATALOGS")

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
    }

    versionCatalogs.create("libs") {
        val minixVersion: String by settings
        val kotlinVersion: String by settings
        val conventions = kotlinVersion.plus("-").plus(minixVersion.substringAfterLast('.'))

        from("dev.racci:catalog:$conventions")
    }
}

rootProject.name = "Minix"

include(":minix-modules:module-common")
include(":minix-modules:module-autoscanner")
include(":minix-modules:module-data")
include(":minix-modules:module-wrappers")
include(":minix-modules:module-flowbus")
include(":minix-modules:module-integrations")
//include(":minix-modules:module-updater")
