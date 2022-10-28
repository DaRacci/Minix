// Workaround for (https://youtrack.jetbrains.com/issue/KTIJ-19369)
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    compileOnly("org.purpurmc.purpur:purpur-api:1.19.2-R0.1-SNAPSHOT")

    compileOnlyApi(libs.exposed.core)
    compileOnlyApi(libs.exposed.dao)
    compileOnlyApi(libs.configurate)
    compileOnlyApi(libs.configurate.extra.kotlin)
    compileOnlyApi(libs.kotlinx.serialization.core)
}
