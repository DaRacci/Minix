dependencies {
    api(libs.ktor.client.core)
    api(libs.ktor.client.cio)

    compileOnlyApi(libs.configurate)
    compileOnly("com.google.code.gson:gson:2.9.1")
}
