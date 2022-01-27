import Dev_racci_minix_platform_gradle.Deps
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder

val minixVersion: String by project

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
    implementation(Deps.kotlin.stdlib)
    implementation(Deps.kotlin.reflect)
    implementation(Deps.kotlinx.dateTime)
    implementation(Deps.kotlinx.coroutines)
    implementation(Deps.kotlinx.immutableCollections)
    implementation(Deps.kotlinx.serialization.json)
    implementation(Deps.kotlinx.serialization.kaml)
    implementation(Deps.exposed.core)
    implementation(Deps.exposed.dao)
    implementation(Deps.minecraft.inventoryFramework)
    implementation(Deps.koin.core)
    implementation(Deps.logging.slf4jAPI)
    implementation(Deps.logging.sentry)
    implementation(Deps.logging.kotlinLogger)

    implementation(Deps.adventure.api)
    implementation(Deps.adventure.kotlin)
    implementation(Deps.adventure.minimessage)

    implementation(Deps.minecraft.acfPaper)
    implementation("dev.racci:Minix-Platform-Loader:0.2.28")
}

bukkit {
    name = "Minix"
    prefix = "Minix"
    author = "Racci"
    apiVersion = "1.18"
    version = rootProject.version.toString()
    main = "dev.racci.minix.core.Minix"
    load = PluginLoadOrder.STARTUP
    softDepend = listOf(
        "LuckPerms",
        "PlaceholderAPI",
        "floodgate",
        "Lands",
        "Aurelium",
    )
    loadBefore = listOf(
        "Sylph",
        "SylphEvents",
        "Sylphia",
        "BloodMoon",
    )
    website = "https://minix.racci.dev/"
}

tasks.shadowJar {
    relocate("dev.racci.minix.platforms", "dev.racci.libs.platforms")
}

tasks.compileKotlin {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}
