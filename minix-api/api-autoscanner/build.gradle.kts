plugins {
    `java-library`
}

val slim: Configuration by configurations.getting

dependencies {
//    compileOnly(project(":api-common"))

    slim(libs.classgraph)
}
