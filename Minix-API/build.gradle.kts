import Dev_racci_minix_platform_gradle.Deps

plugins {
    id("dev.racci.minix.publication")
    kotlin("plugin.serialization")
}

dependencies {
    api(Deps.adventure.api)
    api(Deps.adventure.kotlin)
    api(Deps.adventure.minimessage)

    api(Deps.minecraft.acfPaper)
    api(Deps.minecraft.mcCoroutineAPI)
    api(Deps.minecraft.mcCoroutineCore)

    api(Deps.kotlinx.coroutines)
    api(Deps.kotlinx.serialization.json)
    api(Deps.kotlinx.serialization.kaml)
}
