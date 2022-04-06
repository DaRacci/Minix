import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import org.jetbrains.dokka.utilities.cast

val lib: Configuration by configurations.creating
extensions.getByType<SourceSetContainer>().named(SourceSet.MAIN_SOURCE_SET_NAME) {
    configurations.getByName(compileClasspathConfigurationName).extendsFrom(lib)
    configurations.getByName(runtimeClasspathConfigurationName).extendsFrom(lib)
    configurations.getByName(apiElementsConfigurationName).extendsFrom(lib)
}

dependencies {
    api(libs.minecraft.bstats)
    // We Shade these two due to the puffer fish conflict
    implementation(libs.sentry.core)
    implementation(libs.sentry.kotlin)

    lib(rootProject.libs.bundles.kyori)
    lib(rootProject.libs.bundles.kotlin)
    lib(rootProject.libs.bundles.kotlinx)
    lib(rootProject.libs.kotlinx.serialization.core)
    lib(rootProject.libs.bundles.exposed)

    lib(rootProject.libs.koin.core)
    lib(rootProject.libs.ktor.client.core)
    lib(rootProject.libs.ktor.client.cio)

    lib(rootProject.libs.cloud.core)
    lib(rootProject.libs.cloud.minecraft.paper)
    lib(rootProject.libs.cloud.minecraft.extras)
    lib(rootProject.libs.cloud.kotlin.extensions)
    lib(rootProject.libs.cloud.kotlin.coroutines)

    lib(rootProject.libs.configurate.hocon)
    lib(rootProject.libs.adventure.configurate)
    lib(rootProject.libs.configurate.extra.kotlin)

    lib(rootProject.libs.sentry.core)
    lib(rootProject.libs.sentry.kotlin)
    lib(rootProject.libs.mordant)
    lib(rootProject.libs.caffeine)
    lib(rootProject.libs.reflections)
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
