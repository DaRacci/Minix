

dependencies {
    compileOnly(project(":Minix-API"))
    compileOnly("dev.racci:slimjar:1.3.1")
}

java.withSourcesJar()

tasks.dokkaHtml {
    outputDirectory.set(file("/docs"))
}

tasks.withType<GenerateModuleMetadata> {
    enabled = false
}

dependencies {
    compileOnly(rootProject.libs.minecraft.api.landsAPI)
    compileOnly(rootProject.libs.bundles.cloud.kotlin)
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
