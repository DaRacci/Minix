plugins {
    `java-library`
}

val slim: Configuration by configurations.getting

dependencies {
    slim(libs.classgraph)
}
