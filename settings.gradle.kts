rootProject.name = "RacciCore"

enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    defaultLibrariesExtensionName.set("libs")
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
        maven("https://repo.citizensnpcs.co/")
        maven("https://libraries.minecraft.net/")
        maven("https://dl.bintray.com/kotlin/kotlin-dev/")
        maven("https://repo.aikar.co/content/groups/aikar/")
        maven("https://eldonexus.de/repository/maven-public")
        maven("https://nexus.frengor.com/repository/public/")
        maven("https://repo.dmulloy2.net/repository/public/")
        maven("https://repo.mattstudios.me/artifactory/public/")
        maven("https://repo.codemc.org/repository/maven-public/")
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.onarandombox.com/content/groups/public/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
}

