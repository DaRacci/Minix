// Workaround for (https://youtrack.jetbrains.com/issue/KTIJ-19369)
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `java-library`
    alias(libs.plugins.kotlin.atomicfu)
    alias(libs.plugins.kotlin.serialization)
}

val slim: Configuration by configurations.getting

dependencies {
    slim(libs.arrow.core)
    slim(libs.arrow.optics)
    slim(libs.arrow.fx.stm)
    slim(libs.arrow.fx.coroutines)

    slim(libs.koin.core)
    slim(libs.adventure.api)
    slim(libs.adventure.minimessage)

    slim(libs.kotlin.stdlib)
    slim(libs.kotlin.reflect)
    slim(libs.kotlinx.dateTime)
    slim(libs.kotlinx.atomicfu)
    slim(libs.kotlinx.coroutines)
    slim(libs.kotlinx.immutableCollections)

    compileOnly(libs.configurate)
    compileOnly(libs.configurate.extra.kotlin)
    compileOnly(libs.kotlinx.serialization.core)

    compileOnlyApi("io.insert-koin:koin-annotations:1.0.3")
    compileOnlyApi(libs.apiguardian)

    testApi(libs.koin.core)
}
