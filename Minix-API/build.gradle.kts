plugins {
    `maven-publish`
}

val configuration: Configuration by configurations.creating

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

tasks.assemble.get().dependsOn(sourcesJar)

publishing {
    repositories.maven("https://repo.racci.dev/releases") {
        name = "RacciRepo"
        credentials(PasswordCredentials::class)
    }

    publications.register("maven", MavenPublication::class) {
        from(components["java"])
    }

    dependencies {
        compileOnly(rootProject.libs.mordant)
        compileOnly(rootProject.libs.exposed.core)
        compileOnly(rootProject.libs.exposed.dao)
    }
}
