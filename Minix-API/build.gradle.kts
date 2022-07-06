import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import org.jetbrains.dokka.utilities.cast
import java.net.URI

val lib: Configuration by configurations.creating
val libSlim: Configuration by configurations.creating

extensions.getByType<SourceSetContainer>().named(SourceSet.MAIN_SOURCE_SET_NAME) {
    configurations.getByName(compileClasspathConfigurationName).extendsFrom(lib)
    configurations.getByName(runtimeClasspathConfigurationName).extendsFrom(lib)
    configurations.getByName(apiElementsConfigurationName).extendsFrom(lib)

    configurations.getByName(compileClasspathConfigurationName).extendsFrom(libSlim)
    configurations.getByName(runtimeClasspathConfigurationName).extendsFrom(libSlim)
    configurations.getByName(apiElementsConfigurationName).extendsFrom(libSlim)
}

dependencies {
    // Has to be shaded
    api(rootProject.libs.minecraft.bstats)
    // We Shade these two due to the puffer fish conflict
    api(rootProject.libs.sentry.core)
    api(rootProject.libs.sentry.kotlin)
    // Shade these due to conflict with eco
    libSlim(rootProject.libs.bundles.kotlin)
    libSlim(rootProject.libs.bundles.kotlinx)
    libSlim(rootProject.libs.kotlinx.serialization.json)
    libSlim(rootProject.libs.bundles.exposed)
    libSlim(rootProject.libs.caffeine)
    // Because of the kotlin shade
    libSlim(rootProject.libs.adventure.kotlin)
    libSlim(rootProject.libs.koin.core)
    libSlim(rootProject.libs.ktor.client.core)
    libSlim(rootProject.libs.ktor.client.cio)
    libSlim(rootProject.libs.cloud.kotlin.extensions)
    libSlim(rootProject.libs.cloud.kotlin.coroutines)
    libSlim(rootProject.libs.configurate.extra.kotlin)

    lib(rootProject.libs.adventure.api)
    lib(rootProject.libs.adventure.minimessage)
    libSlim(rootProject.libs.adventure.configurate)

    libSlim(rootProject.libs.cloud.core)
    libSlim(rootProject.libs.cloud.minecraft.paper)
    libSlim(rootProject.libs.cloud.minecraft.extras)

    lib(rootProject.libs.configurate.hocon)
    libSlim(rootProject.libs.mordant)

    libSlim("io.github.classgraph:classgraph:4.8.146")
}

// Lmao this works well as compared to what I was doing before
rootProject.extensions.getByName("bukkit").cast<BukkitPluginDescription>().libraries = lib.dependencies.map { "${it.group}:${it.name}:${it.version}" }

java.withSourcesJar()

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
            val depNode = groovy.util.Node(asNode(), "dependencies")
            arrayOf(
                *configurations["lib"].resolvedConfiguration.firstLevelModuleDependencies.toTypedArray(),
                *configurations["libSlim"].resolvedConfiguration.firstLevelModuleDependencies.toTypedArray()
            ).forEach { dep ->
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
