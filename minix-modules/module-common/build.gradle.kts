// Workaround for (https://youtrack.jetbrains.com/issue/KTIJ-19369)
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `java-library`
    alias(libs.plugins.kotlin.plugin.atomicfu)
    alias(libs.plugins.kotlin.plugin.serialization)
}

dependencies {
    api(libs.arrow.core)
    api(libs.arrow.optics)
    api(libs.arrow.fx.stm)
    api(libs.arrow.fx.coroutines)

    api(libs.koin.core)
    api(libs.adventure.api)
    api(libs.adventure.minimessage)

    api(libs.kotlin.stdlib)
    api(libs.kotlin.reflect)
    api(libs.kotlinx.dateTime)
    api(libs.kotlinx.atomicfu)
    api(libs.kotlinx.coroutines)
    api(libs.kotlinx.immutableCollections)

    compileOnly(libs.configurate)
    compileOnly(libs.configurate.extra.kotlin)
    compileOnly(libs.kotlinx.serialization.core)

    compileOnlyApi(libs.koin.annotations)
    compileOnlyApi(libs.apiguardian)

    testApi(libs.koin.core)
}
