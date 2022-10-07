import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URL

val minixVersion: String by project
val version: String by project

plugins {
    id("dev.racci.minix.kotlin")
    id("dev.racci.minix.copyjar")
    id("dev.racci.minix.purpurmc")
    id("dev.racci.minix.nms")

    id("org.jetbrains.dokka") version "1.7.10"
    kotlin("plugin.atomicfu")
    kotlin("plugin.serialization")

    id("dev.racci.slimjar")
    id("net.minecrell.plugin-yml.bukkit")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
}

apiValidation {
    ignoredProjects = subprojects.first { it.name == "minix-core" }.subprojects.map(Project::getName).toMutableSet()
    ignoredProjects += "Minix"
    ignoredProjects += "Minix-Core"
    ignoredProjects += "minix-core"

    ignoredPackages.add("dev.racci.minix.core")
    nonPublicMarkers.add("dev.racci.minix.api.MinixInternal")
}

// TODO -> Migrate Minix-NMS to Minix instead of Minix-Conventions
dependencies {
    implementation(project("Minix-Core"))
    implementation(project("Minix-API"))
    implementation("dev.racci.slimjar:slimjar:1.2.11")
    implementation(libs.minecraft.bstats)
}

kotlin {
    kotlinDaemonJvmArgs = listOf("-Xemit-jvm-type-annotations")
}

bukkit {
    name = "Minix"
    prefix = "Minix"
    author = "Racci"
    apiVersion = "1.19"
    version = rootProject.version.toString()
    main = "dev.racci.minix.core.MinixInit"
    load = PluginLoadOrder.STARTUP
    loadBefore = listOf("eco")
    softDepend = listOf("PlaceholderAPI", "Lands", "ServerUtils")
    website = "https://github.com/DaRacci/Minix"
}

tasks {
    val quickBuild by creating {
        group = "build"
        dependsOn(compileKotlin)
        dependsOn(shadowJar)
        dependsOn(reobfJar)
        findByName("copyJar")?.let { dependsOn(it) }
    }
}

subprojects {

    apply(plugin = "dev.racci.minix.kotlin")
    apply(plugin = "dev.racci.minix.purpurmc")
    apply(plugin = "dev.racci.minix.nms")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "maven-publish")

    dependencies {
        testImplementation(platform(kotlin("bom")))
        testImplementation(rootProject.libs.bundles.kotlin)
        testImplementation(rootProject.libs.bundles.kotlinx)
        testImplementation(rootProject.libs.bundles.testing)
        testImplementation(rootProject.libs.koin.test)
        testImplementation(rootProject.libs.minecraft.bstats)
        testImplementation(project(":Minix-API"))
    }

    tasks {

        withType<Test>().configureEach {
            useJUnitPlatform()
        }

        withType<KotlinCompile>().configureEach {
            kotlinOptions.freeCompilerArgs += "-opt-in=dev.racci.minix.api.annotations.MinixInternal"
        }

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
    }

    afterEvaluate {
        val subSlim = this.configurations.findByName("slim") ?: return@afterEvaluate
        subSlim.dependencies.forEach {
            rootProject.dependencies {
                slim(it)
            }
        }
    }
}

fun included(
    build: String,
    task: String
) = gradle.includedBuild(build).task(task)

inline fun <reified T : Task> TaskProvider<T>.alsoSubprojects(crossinline block: T.() -> Unit = {}) {
    this {
        dependsOn(gradle.includedBuilds.map { it.task(":$name") })
        block()
    }
}

tasks {

    shadowJar {
        dependencyFilter.include {
            it.module.id.module == libs.sentry.core.get().module ||
                it.module.id.module == libs.sentry.kotlin.get().module ||
                it.moduleGroup == libs.minecraft.bstats.get().module.group ||
                it.moduleGroup == "dev.racci.slimjar" ||
                it.moduleName == "Minix-Core" ||
                it.moduleName == "Minix-API" ||
                it.moduleName == "Minix-NMS" ||
                it.moduleName == "core-integrations" ||
                it.moduleName == "jvm-driver"
        }
        val prefix = "dev.racci.minix.libs"
        relocate("io.sentry", "$prefix.io.sentry")
        relocate("org.bstats", "$prefix.org.bstats")
        relocate("io.github.slimjar", "$prefix.io.github.slimjar")
    }

    ktlintFormat.alsoSubprojects()
    build.alsoSubprojects()
    clean.alsoSubprojects()

    withType<org.jetbrains.dokka.gradle.DokkaMultiModuleTask> {
        outputDirectory.set(File("$rootDir/docs"))
    }
}

allprojects {
    configurations {
        testImplementation.get().exclude("org.jetbrains.kotlin", "kotlin-test-junit")

        // These refuse to resolve sometimes
        configureEach {
            exclude("me.carleslc.Simple-YAML", "Simple-Configuration")
            exclude("me.carleslc.Simple-YAML", "Simple-Yaml")
            exclude("com.github.technove", "Flare")
        }
    }
}
