import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import org.jetbrains.dokka.utilities.cast

val lib: Configuration by configurations.creating
extensions.getByType<SourceSetContainer>().named(SourceSet.MAIN_SOURCE_SET_NAME) {
    configurations.getByName(compileClasspathConfigurationName).extendsFrom(lib)
    configurations.getByName(runtimeClasspathConfigurationName).extendsFrom(lib)
    configurations.getByName(apiElementsConfigurationName).extendsFrom(lib)
}

dependencies {
    // Has to be shaded
    api(libs.minecraft.bstats)
    // We Shade these two due to the puffer fish conflict
    api(libs.sentry.core)
    api(libs.sentry.kotlin)
    // Shade these due to conflict with eco
    api(rootProject.libs.bundles.kotlin)
    api(rootProject.libs.bundles.kotlinx)
    api(rootProject.libs.kotlinx.serialization.json)
    api(rootProject.libs.bundles.exposed)
    api(rootProject.libs.caffeine)
    // Because of the kotlin shade
    api(rootProject.libs.adventure.kotlin)
    api(rootProject.libs.koin.core)
    api(rootProject.libs.ktor.client.core)
    api(rootProject.libs.ktor.client.cio)
    api(rootProject.libs.cloud.kotlin.extensions)
    api(rootProject.libs.cloud.kotlin.coroutines)
    api(rootProject.libs.configurate.extra.kotlin)

    lib(rootProject.libs.adventure.api)
    lib(rootProject.libs.adventure.minimessage)
    lib(rootProject.libs.adventure.configurate)

    lib(rootProject.libs.cloud.core)
    lib(rootProject.libs.cloud.minecraft.paper)
    lib(rootProject.libs.cloud.minecraft.extras)

    lib(rootProject.libs.configurate.hocon)
    lib(rootProject.libs.mordant)

    lib("io.github.classgraph:classgraph:4.8.146")
}

// Lmao this works well as compared to what I was doing before
rootProject.extensions.getByName("bukkit").cast<BukkitPluginDescription>().libraries = lib.dependencies.map { "${it.group}:${it.name}:${it.version}" }

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
                depNode.children().cast<groovy.util.NodeList>().firstOrNull {
                    val node = it as groovy.util.Node
                    node["groupId"] == dep.moduleGroup &&
                        node["artifactId"] == dep.moduleName &&
                        node["version"] == dep.moduleVersion &&
                        node["scope"] == "compile"
                } ?: run {
                    val node = groovy.util.Node(depNode, "dependency")
                    node.appendNode("groupId", dep.moduleGroup)
                    node.appendNode("artifactId", dep.moduleName)
                    node.appendNode("version", dep.moduleVersion)
                    node.appendNode("scope", "compile")
                }
            }
        }
    }
}
