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
//    implementation("net.kyori:adventure-platform-bukkit:4.0.1")
    implementation("org.bstats:bstats-bukkit:3.0.0")
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
    loadBefore = listOf("Terix")
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
        libs.exposed.dateTime.get().toString(),
        libs.hikariCP.get().toString(),
        libs.koin.core.get().toString(),
        libs.logging.sentry.get().toString(),
        libs.mordant.get().toString(),
        libs.caffeine.get().toString(),
        libs.kotlinx.serialization.kaml.get().toString()
    )
    website = "https://github.com/DaRacci/Minix"
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

        testImplementation(platform("org.junit:junit-bom:5.8.2"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testImplementation(kotlin("test"))
        testImplementation("io.strikt:strikt-core:0.34.1")
    }

    tasks {

        test.get().useJUnitPlatform()

        dokkaHtml.get().dokkaSourceSets.configureEach {
            includeNonPublic.set(false)
            skipEmptyPackages.set(true)
            displayName.set(project.name.split("-")[1])
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

        jar.get().archiveFileName.set("${project.name}-${project.version}.jar")
    }
}

subprojects {
    dependencies {
        compileOnly(rootProject.libs.adventure.kotlin)
        compileOnly("org.bstats:bstats-bukkit:3.0.0")
//        compileOnly("net.kyori:adventure-platform-bukkit:4.0.1")
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
        relocate("org.bstats", "$location.bstats")
        relocate("dev.racci.minix.nms", "$location.nms")
        relocate("net.kyori.adventure.extra", "$location.adventure.extra")
//        relocate("net.kyori.adventure.platform", "$location.adventure.platform")
//        relocate("net.kyori.adventure.text.serializer.craftbukkit", "$location.adventure.text.serializer.craftbukkit")
        dependencies {
            exclude {
                it.moduleName.startsWith("examination") ||
                    it.moduleName.startsWith("kotlin") ||
                    it.moduleName == "adventure-api" ||
                    it.moduleName == "adventure-key" ||
                    it.moduleName == "adventure-nbt" ||
                    it.moduleName.startsWith("adventure-text-serializer") ||
                    it.moduleName.contains("annotations")
            }
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
