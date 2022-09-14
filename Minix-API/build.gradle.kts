@file:Suppress("UnstableApiUsage")

import groovy.util.Node
import groovy.util.NodeList
import java.net.URI

val minixVersion: String by rootProject
val slim: Configuration by configurations.creating

configurations {
    compileClasspath.get().extendsFrom(slim)
    runtimeClasspath.get().extendsFrom(slim)
    api.get().extendsFrom(slim)
}

dependencies {
    slim("dev.racci:Minix-NMS:$minixVersion")

    // Global log tracker
    slim(libs.sentry.core)
    slim(libs.sentry.kotlin)

    // Kotlin libraries
    slim(libs.bundles.kotlin)
    slim(libs.bundles.kotlinx)
    slim(libs.kotlinx.serialization.core)

    // Networking
    slim(libs.ktor.client.cio)
    slim(libs.ktor.client.core)

    // Injection Manager
    slim(libs.koin.core)
    slim(libs.koin.ktor)

    // Because of the kotlin shade
    slim(libs.adventure.kotlin)

    // Minix Logger
    slim(libs.mordant)

    // Minix Config
    slim(libs.configurate)
    slim(libs.configurate.hocon)
    slim(libs.configurate.extra.kotlin)

    // Adventure
    slim(libs.adventure.api)
    slim(libs.adventure.kotlin)
    slim(libs.adventure.minimessage)
    slim(libs.adventure.configurate)

    // Misc
    slim(libs.caffeine)
    slim(libs.minecraft.bstats)

    // Unused Libraries for Minix Consumers

    slim(libs.minecraft.particles)

    // Supported Command framework
    slim(libs.cloud.core)
    slim(libs.cloud.minecraft.paper)
    slim(libs.cloud.minecraft.extras)
    slim(libs.cloud.kotlin.extensions)
    slim(libs.cloud.kotlin.coroutines)

    // Database libraries
    slim(libs.bundles.exposed)
    slim(libs.h2)

    // Reflection loading for extensions and more
    slim(libs.classgraph)
}

java.withSourcesJar()
repositories {
    mavenCentral()
}

tasks {

    dokkaHtml.get().outputDirectory.set(file("/docs"))

    withType<GenerateModuleMetadata> { enabled = false }
}

publishing {
    repositories.maven {
        name = "RacciRepo"
        url = URI("https://repo.racci.dev/${if (version.toString().endsWith("-SNAPSHOT")) "snapshots" else "releases"}")
        credentials(PasswordCredentials::class)
    }

    publications.register("maven", MavenPublication::class) {
        artifactId = rootProject.name
        artifact(tasks.reobfJar.get().outputJar) {
            builtBy(tasks.reobfJar)
            classifier = null
        }
        artifact(tasks.kotlinSourcesJar) {
            builtBy(tasks.kotlinSourcesJar)
            classifier = "sources"
        }
        pom.withXml {
            val depNode = Node(asNode(), "dependencies")
            val nodeList = depNode.children() as NodeList

            fun ResolvedDependency.getScope() = if (this.configuration == "runtime") "runtimeElements" else this.configuration

            fun ResolvedDependency.isPresent() = nodeList.any {
                val node = it as Node
                node["groupId"] == this.moduleGroup &&
                    node["artifactId"] == this.moduleName &&
                    node["version"] == this.moduleVersion &&
                    node["scope"] == this.configuration
            }

            configurations["slim"].resolvedConfiguration.firstLevelModuleDependencies.forEach { dep ->
                if (dep.isPresent()) return@forEach

                Node(depNode, "dependency").apply {
                    appendNode("groupId", dep.moduleGroup)
                    appendNode("artifactId", dep.moduleName)
                    appendNode("version", dep.moduleVersion)
                    appendNode("scope", dep.configuration)
                }
            }
        }
    }
}
