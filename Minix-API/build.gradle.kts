import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import org.jetbrains.dokka.utilities.cast

val lib: Configuration by configurations.creating
extensions.getByType<SourceSetContainer>().named(SourceSet.MAIN_SOURCE_SET_NAME) {
    configurations.getByName(compileClasspathConfigurationName).extendsFrom(lib)
    configurations.getByName(runtimeClasspathConfigurationName).extendsFrom(lib)
    configurations.getByName(apiElementsConfigurationName).extendsFrom(lib)
}

val libShade: Configuration by configurations.creating
extensions.getByType<SourceSetContainer>().named(SourceSet.MAIN_SOURCE_SET_NAME) {
    configurations.getByName(compileClasspathConfigurationName).extendsFrom(libShade)
    configurations.getByName(runtimeClasspathConfigurationName).extendsFrom(libShade)
    configurations.getByName(apiElementsConfigurationName).extendsFrom(libShade)
}

dependencies {
    // Has to be shaded
    libShade(libs.minecraft.bstats)
    // We Shade these two due to the puffer fish conflict
    libShade(libs.sentry.core)
    libShade(libs.sentry.kotlin)
    // Shade these due to conflict with eco
    libShade(rootProject.libs.bundles.kotlin)
    libShade(rootProject.libs.bundles.kotlinx)
    libShade(rootProject.libs.kotlinx.serialization.json)
    libShade(rootProject.libs.bundles.exposed)
    libShade(rootProject.libs.caffeine)
    // Because of the kotlin shade
    libShade(rootProject.libs.adventure.kotlin)
    libShade(rootProject.libs.koin.core)
    libShade(rootProject.libs.ktor.client.core)
    libShade(rootProject.libs.ktor.client.cio)
    libShade(rootProject.libs.cloud.kotlin.extensions)
    libShade(rootProject.libs.cloud.kotlin.coroutines)
    libShade(rootProject.libs.configurate.extra.kotlin)

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
            arrayOf(
                *configurations["lib"].resolvedConfiguration.firstLevelModuleDependencies.toTypedArray(),
                *configurations["libShade"].resolvedConfiguration.firstLevelModuleDependencies.toTypedArray()
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
