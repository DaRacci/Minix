import java.net.URI

dependencies {
    compileOnly(project(":Minix-API"))
    compileOnly("dev.racci.slimjar:slimjar:1.2.10")
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

    implementation("io.github.toolfactory:jvm-driver:9.3.0")
    implementation(project(":minix-core:core-integrations"))
}

publishing {
    repositories.maven {
        name = "RacciRepo"
        url = URI("https://repo.racci.dev/${if (version.toString().endsWith("-SNAPSHOT")) "snapshots" else "releases"}")
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

        pom.withXml {
            val depNode = groovy.util.Node(asNode(), "dependencies")
            val nodeList = depNode.children() as groovy.util.NodeList

            fun ResolvedDependency.getScope() = if (this.configuration == "runtime") "runtimeElements" else this.configuration

            fun ResolvedDependency.isPresent() = nodeList.any {
                val node = it as groovy.util.Node
                node["groupId"] == this.moduleGroup &&
                    node["artifactId"] == this.moduleName &&
                    node["version"] == this.moduleVersion &&
                    node["scope"] == this.getScope()
            }

            configurations.implementationDependenciesMetadata.get().resolvedConfiguration.firstLevelModuleDependencies.forEach { dep ->
                if (dep.isPresent()) return@forEach

                groovy.util.Node(depNode, "dependency").apply {
                    appendNode("groupId", dep.moduleGroup)
                    appendNode("artifactId", dep.moduleName)
                    appendNode("version", dep.moduleVersion)
                    appendNode("scope", dep.getScope())
                }
            }
        }
    }
}
