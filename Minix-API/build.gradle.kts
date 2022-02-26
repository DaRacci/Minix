import org.jetbrains.dokka.utilities.cast

plugins {
    `maven-publish`
}
val lib by configurations.creating
extensions.getByType<SourceSetContainer>().named(SourceSet.MAIN_SOURCE_SET_NAME) {
    configurations.getByName(compileClasspathConfigurationName).extendsFrom(lib)
    configurations.getByName(runtimeClasspathConfigurationName).extendsFrom(lib)
}

dependencies {
    lib(rootProject.libs.kotlin.stdlib)
    lib(rootProject.libs.kotlin.reflect)
    lib(rootProject.libs.kotlinx.dateTime)
    lib(rootProject.libs.kotlinx.coroutines)
    lib(rootProject.libs.kotlinx.immutableCollections)
    lib(rootProject.libs.kotlinx.serialization.json)
    lib(rootProject.libs.exposed.core)
    lib(rootProject.libs.exposed.dao)
    lib(rootProject.libs.exposed.jdbc)
    lib(rootProject.libs.exposed.dateTime)
    lib(rootProject.libs.hikariCP)
    lib(rootProject.libs.koin.core)
    lib(rootProject.libs.logging.sentry)
    lib(rootProject.libs.mordant)
    lib(rootProject.libs.caffeine)
    lib(rootProject.libs.kotlinx.serialization.kaml)
    lib(rootProject.libs.minecraft.bstats)
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
            val depNode = groovy.util.Node(asNode(), "dependencies")
            configurations["lib"].resolvedConfiguration.firstLevelModuleDependencies.forEach { dep ->
                depNode.children().cast<groovy.util.NodeList>().firstOrNull() {
                    val node = it as groovy.util.Node
                    node["groupId"] == dep.moduleGroup &&
                        node["artifactId"] == dep.moduleName &&
                        node["version"] == dep.moduleVersion &&
                        node["optional"] == "true" &&
                        node["scope"] == "provided"
                } ?: run {
                    val node = groovy.util.Node(depNode, "dependency")
                    node.appendNode("groupId", dep.moduleGroup)
                    node.appendNode("artifactId", dep.moduleName)
                    node.appendNode("version", dep.moduleVersion)
                    node.appendNode("optional", "true")
                    node.appendNode("scope", "provided")
                }
            }
        }
    }
}
