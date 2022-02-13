import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder
import java.net.URL

val minixVersion: String by project
val version: String by project

plugins {
    id("dev.racci.minix.kotlin")
    id("dev.racci.minix.copyjar")
    id("dev.racci.minix.purpurmc")
    id("dev.racci.minix.nms")
    id("org.jetbrains.dokka")
    kotlin("plugin.serialization")
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    implementation(project("Minix-Core"))
    implementation(project("Minix-API"))
    implementation(libs.adventure.kotlin)
    implementation(libs.adventure.minimessage)
    implementation("dev.racci:Minix-NMS:$minixVersion")
}

bukkit {
    name = "Minix"
    prefix = "Minix"
    author = "Racci"
    apiVersion = "1.18"
    version = rootProject.version.toString()
    main = "dev.racci.minix.core.MinixImpl"
    load = PluginLoadOrder.STARTUP
    loadBefore = listOf("Sylphia")
    libraries = listOf(
        libs.kotlin.stdlib.get().toString(),
        libs.kotlin.reflect.get().toString(),
        libs.kotlinx.dateTime.get().toString(),
        libs.kotlinx.coroutines.get().toString(),
        libs.kotlinx.immutableCollections.get().toString(),
        libs.kotlinx.serialization.json.get().toString(),
        libs.exposed.core.get().toString(),
        libs.exposed.dao.get().toString(),
        libs.exposed.jdbc.get().toString(),
        libs.hikariCP.get().toString(),
        libs.koin.core.get().toString(),
        libs.logging.sentry.get().toString(),
        libs.mordant.get().toString(),
        libs.caffeine.get().toString(),
        libs.adventure.kotlin.get().toString(),
        "org.bstats:bstats-bukkit:3.0.0",
    )
    website = "https://minix.racci.dev/"
}

allprojects {

    apply(plugin = "dev.racci.minix.kotlin")
    apply(plugin = "dev.racci.minix.purpurmc")
    apply(plugin = "dev.racci.minix.nms")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "org.jetbrains.dokka")

    dependencies {
        compileOnly(rootProject.libs.kotlin.stdlib)
        compileOnly(rootProject.libs.kotlin.reflect)
        compileOnly(rootProject.libs.kotlinx.dateTime)
        compileOnly(rootProject.libs.kotlinx.immutableCollections)
        compileOnly(rootProject.libs.kotlinx.serialization.json)
        compileOnly(rootProject.libs.kotlinx.coroutines)
        compileOnly(rootProject.libs.koin.core)
        compileOnly(rootProject.libs.caffeine)
        compileOnly("org.bstats:bstats-bukkit:3.0.0")

        testImplementation(platform("org.junit:junit-bom:5.8.2"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testImplementation(kotlin("test"))
        testImplementation("io.strikt:strikt-core:0.34.1")
    }

    tasks {

        test {
            useJUnitPlatform()
        }

        dokkaHtml {
            dokkaSourceSets.configureEach {
                includeNonPublic.set(false)
                skipEmptyPackages.set(true)
                displayName.set(this@dokkaHtml.project.name.split("-")[1])
                platform.set(org.jetbrains.dokka.Platform.jvm)
                sourceLink {
                    localDirectory.set(file("src/main/kotlin"))
                    remoteUrl.set(URL("https://github.com/DaRacci/Minix/blob/master/src/main/kotlin"))
                    remoteLineSuffix.set("#L")
                }
                jdkVersion.set(17)
                externalDocumentationLink {
                    url.set(URL("https://minix.racci.dev/"))
                }
            }
        }

        jar.get().archiveFileName.set("${project.name}-${project.version}.jar")
    }
}

fun included(
    build: String,
    task: String
) = gradle.includedBuild(build).task(task)

tasks {

    shadowJar {
        archiveFileName.set("${project.name}-${project.version}-all.jar")
        val location = "dev.racci.minix.libs"
        relocate("net.kyori.adventure.text.minimessage", "$location.kyori.minimessage")
        relocate("dev.racci.minix.nms", "$location.nms")
        dependencies {
            include(project("Minix-Core"))
            include(project("Minix-API"))
            include(dependency(rootProject.libs.adventure.kotlin.get()))
            include(dependency(rootProject.libs.adventure.minimessage.get()))
        }
    }

    ktlintFormat {
        dependsOn(gradle.includedBuilds.map { it.task(":ktlintFormat") })
    }

    build {
        dependsOn(gradle.includedBuilds.map { it.task(":build") })
    }

    clean {
        dependsOn(gradle.includedBuilds.map { it.task(":clean") })
    }

    withType<org.jetbrains.dokka.gradle.DokkaMultiModuleTask> {
        outputDirectory.set(File("$rootDir/docs"))
    }
}
