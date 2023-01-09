dependencies {
    compileOnlyApi(libs.caffeine)
    compileOnlyApi(libs.classgraph)

    testImplementation(libs.testing.kotest.junit5)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
