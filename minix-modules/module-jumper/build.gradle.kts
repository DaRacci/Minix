repositories {
    maven("https://repo.racci.dev/releases/") {
        name = "Slimjar Repository"
        mavenContent {
            releasesOnly()
            includeGroup("dev.racci.slimjar")
        }
    }
}

dependencies {
    compileOnlyApi(libs.slimjar)
}
