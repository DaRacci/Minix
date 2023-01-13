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
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)

    repositories {
        fun racciRepo(subPath: String, f: MavenRepositoryContentDescriptor.() -> Unit) =
            maven("https://repo.racci.dev/$subPath") {
                name = "RacciRepo $subPath"
                mavenContent {
                    f()
                    includeGroupByRegex("dev.racci([.a-z]+)?")
                }
            }

        mavenCentral()
        maven("https://jitpack.io")
        racciRepo("releases") { releasesOnly() }
        racciRepo("snapshots") { snapshotsOnly() }
        maven("https://repo.papermc.io/repository/maven-public/")

        maven("https://repo.purpurmc.org/snapshots") {
            mavenContent {
                includeGroup("org.purpurmc.purpur")
            }
        }

        maven("https://repo.fvdh.dev/releases") {
            mavenContent {
                releasesOnly()
                includeGroup("net.frankheijden.serverutils")
            }
        }

        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") {
            mavenContent {
                releasesOnly()
                includeModule("me.clip", "placeholderapi")
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
include("minix-plugin")
include("minix-modules")

val excludes = arrayOf("updater", "wrappers")
for (dir in file("minix-modules").listFiles()!!) {
    if (dir.name.substringAfter('-') in excludes || !dir.isDirectory) continue
    include(":minix-modules:${dir.name}")
}
