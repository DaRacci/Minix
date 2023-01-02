import java.net.URI

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
}

publishing {
    repositories.maven {
        name = "RacciRepo"
        url = URI("https://repo.racci.dev/${if (version.toString().endsWith("-SNAPSHOT")) "snapshots" else "releases"}")
        credentials(PasswordCredentials::class)
    }

    publications.register("maven", MavenPublication::class) {
        artifactId = "core-integrations"
        from(components["java"])
//        artifact(tasks.reobfJar.get().outputJar) {
//            builtBy(tasks.reobfJar)
//            classifier = null
//        }
//        artifact(tasks.kotlinSourcesJar) {
//            builtBy(tasks.kotlinSourcesJar)
//            classifier = "sources"
//        }
    }
}
