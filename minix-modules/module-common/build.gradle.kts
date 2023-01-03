// Workaround for (https://youtrack.jetbrains.com/issue/KTIJ-19369)
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.plugin.atomicfu)
    alias(libs.plugins.kotlin.plugin.serialization)
}

dependencies {
    compileOnlyApi(libs.arrow.core)
    compileOnlyApi(libs.arrow.optics)
    compileOnlyApi(libs.arrow.fx.stm)
    compileOnlyApi(libs.arrow.fx.coroutines)

    compileOnlyApi(libs.koin.core)
    compileOnlyApi(libs.adventure.api)
    compileOnlyApi(libs.adventure.minimessage)

    compileOnlyApi(libs.kotlin.stdlib)
    compileOnlyApi(libs.kotlin.reflect)
    compileOnlyApi(libs.kotlinx.dateTime)
    compileOnlyApi(libs.kotlinx.atomicfu)
    compileOnlyApi(libs.kotlinx.coroutines)
    compileOnlyApi(libs.kotlinx.immutableCollections)

    compileOnly(libs.configurate)
    compileOnly(libs.configurate.extra.kotlin)
    compileOnly(libs.kotlinx.serialization.core)

    compileOnlyApi(libs.koin.annotations)
    compileOnlyApi(libs.apiguardian)

    testApi(libs.koin.core)
}
