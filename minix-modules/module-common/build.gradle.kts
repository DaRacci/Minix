// Workaround for (https://youtrack.jetbrains.com/issue/KTIJ-19369)
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `java-library`
    alias(libs.plugins.kotlin.atomicfu)
    alias(libs.plugins.kotlin.serialization)
}

val slim: Configuration by configurations.getting

dependencies {
    slim(platform("io.arrow-kt:arrow-stack:1.1.4-alpha.10"))
    slim("io.arrow-kt:arrow-core")
    slim("io.arrow-kt:arrow-fx-coroutines")
    slim("io.arrow-kt:arrow-optics")

    slim(libs.adventure.api)
    slim(libs.adventure.minimessage)

    slim(libs.kotlin.stdlib)
    slim(libs.kotlin.reflect)
    slim(libs.kotlinx.dateTime)
    slim(libs.kotlinx.atomicfu)
    slim(libs.kotlinx.coroutines)
    slim(libs.kotlinx.immutableCollections)

    slim(libs.koin.core)

    compileOnly(libs.configurate)
    compileOnly(libs.configurate.extra.kotlin)
    compileOnly(libs.kotlinx.serialization.core)

    compileOnlyApi("io.insert-koin:koin-annotations:1.0.3")
    compileOnlyApi("org.apiguardian:apiguardian-api:1.1.2")
}
