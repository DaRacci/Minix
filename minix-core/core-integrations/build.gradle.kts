import java.net.URI

dependencies {
    compileOnly(libs.minecraft.api.landsAPI)
    compileOnly(libs.minecraft.api.placeholderAPI)
    compileOnly("net.frankheijden.serverutils:ServerUtils:3.5.3")
}

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.fvdh.dev/releases")
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
