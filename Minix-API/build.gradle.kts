plugins {
    `maven-publish`
}

java.withSourcesJar()

publishing {
    repositories.maven("https://repo.racci.dev/releases") {
        name = "RacciRepo"
        credentials(PasswordCredentials::class)
    }

    publications.register("maven", MavenPublication::class) {
        artifactId = rootProject.name
        from(components["java"])
    }

    dependencies {
        compileOnly(rootProject.libs.mordant)
        compileOnly(rootProject.libs.exposed.core)
        compileOnly(rootProject.libs.exposed.dao)
    }
}
