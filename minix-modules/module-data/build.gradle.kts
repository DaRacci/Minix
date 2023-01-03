// Workaround for (https://youtrack.jetbrains.com/issue/KTIJ-19369)
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.plugin.serialization)
}

dependencies {
    compileOnlyApi(libs.exposed.core)
    compileOnlyApi(libs.exposed.dao)
    compileOnlyApi(libs.configurate)
    compileOnlyApi(libs.configurate.extra.kotlin)
    compileOnlyApi(libs.kotlinx.serialization.core)
}
