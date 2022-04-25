

dependencies {
    compileOnly(project(":Minix-API"))
}

java.withSourcesJar()

tasks.dokkaHtml {
    outputDirectory.set(file("/docs"))
}

tasks.withType<GenerateModuleMetadata> {
    enabled = false
}

publishing {
    repositories.maven("https://repo.racci.dev/releases") {
        name = "RacciRepo"
        credentials(PasswordCredentials::class)
    }
    repositories.maven("https://repo.racci.dev/snapshots") {
        name = "RacciSnapshots"
        credentials(PasswordCredentials::class)
    }

    publications.register("maven", MavenPublication::class) {
        artifactId = rootProject.name + "-Core"
        artifact(tasks.reobfJar.get().outputJar) {
            builtBy(tasks.reobfJar)
            classifier = null
        }
        artifact(tasks.kotlinSourcesJar) {
            builtBy(tasks.kotlinSourcesJar)
            classifier = "sources"
        }
    }
}
