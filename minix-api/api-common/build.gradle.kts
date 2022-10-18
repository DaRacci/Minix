plugins {
    `java-library`
    alias(libs.plugins.kotlin.atomicfu)
}

val slim by configurations.getting

dependencies {
    slim(libs.kotlin.stdlib)
    slim(libs.kotlin.reflect)
    slim(libs.kotlinx.dateTime)
    slim(libs.kotlinx.atomicfu)
    slim(libs.kotlinx.coroutines)
    slim(libs.kotlinx.immutableCollections)

    slim(libs.koin.core)
}
