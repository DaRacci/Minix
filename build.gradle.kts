import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlinx.serialization.gradle.SerializationGradleSubplugin
import java.net.URL

val minixVersion: String by project
val version: String by project

plugins {
    kotlin("multiplatform") version "1.7.20"
    alias(libs.plugins.minix.nms)
//    alias(libs.plugins.minix.kotlin)
    alias(libs.plugins.minix.copyjar)
    alias(libs.plugins.minix.purpurmc)

    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin.atomicfu)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.binaryValidator)

    alias(libs.plugins.slimjar)
    alias(libs.plugins.minecraft.pluginYML)
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

    slim("io.github.toolfactory:jvm-driver:9.3.0")
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
//        dependsOn(compileKotlin)
        dependsOn(shadowJar)
        dependsOn(reobfJar)
        findByName("copyJar")?.let { dependsOn(it) }
    }
}

kotlin {
    explicitApi()

    sourceSets {
        val commonMain by getting {
            this.kotlin.srcDir("minix-api/api-plugin/plugin-common/src/main/kotlin")
            this.kotlin.srcDir("minix-plugin/plugin-common/src/main/resources")

            dependencies {
                api(project(":minix-api:api-autoscanner"))
                api(project(":minix-api:api-common"))

                compileOnly("org.apiguardian:apiguardian-api:1.1.2")

                api(libs.kotlin.stdlib)
                api(libs.kotlin.reflect)
                api(libs.kotlinx.dateTime)
                api(libs.kotlinx.atomicfu)
                api(libs.kotlinx.coroutines)
                api(libs.kotlinx.immutableCollections)

                api(libs.koin.core)
                api(libs.mordant)

                api("org.bstats:bstats-base:3.0.0")
            }
        }

        val paperMain by creating {
            this.kotlin.srcDir("minix-api/api-plugin/plugin-paper/src/main/kotlin")
            this.kotlin.srcDir("minix-plugin/plugin-paper/src/main/kotlin")
            this.dependsOn(commonMain)

            repositories {
                mavenCentral()
                maven("https://papermc.io/repo/repository/maven-public/")
            }

            this.dependencies {
                compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")

                api(libs.minecraft.bstats)
            }
        }

        val velocityMain by creating {
            this.kotlin.srcDir("minix-api/api-plugin/plugin-velocity/src/main/kotlin")
            this.kotlin.srcDir("minix-plugin/plugin-velocity/src/main/kotlin")
            this.dependsOn(commonMain)

            this.dependencies {
                compileOnly("com.velocitypowered:velocity-api:3.1.1")
                compileOnly("com.velocitypowered:velocity-api:3.1.1") // TODO -> KSP

                api("org.bstats:bstats-velocity:3.0.0")
            }
        }
    }

    targets {
        all {
            compilations.all {
                kotlinOptions {
                    allWarningsAsErrors = true
                }
            }
        }

        jvm("paper")
        jvm("velocity")
    }
}

subprojects {
    apply<DokkaPlugin>()
    apply<JavaLibraryPlugin>()
    apply<MavenPublishPlugin>()

    apply<Dev_racci_minix_kotlinPlugin>()
    apply<Dev_racci_minix_copyjarPlugin>()
    apply<Dev_racci_minix_purpurmcPlugin>()
    apply<SerializationGradleSubplugin>()

    configurations {
        val slim by creating

        compileClasspath.get().extendsFrom(slim)
        runtimeClasspath.get().extendsFrom(slim)
//        api.get().extendsFrom(slim)
    }

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

inline fun <reified T : Task> TaskProvider<T>.alsoSubprojects(crossinline block: T.() -> Unit = {}) {
    this {
        dependsOn(gradle.includedBuilds.map { it.task(":$name") })
        block()
    }
}

tasks {

    shadowJar {
        dependencyFilter.include {
            subprojects.map(Project::getName).contains(it.moduleName) ||
                it.moduleGroup == libs.sentry.core.get().module.group ||
                it.moduleGroup == libs.minecraft.bstats.get().module.group ||
                it.moduleGroup == "dev.racci.slimjar"
        }
        val prefix = "dev.racci.minix.libs"
        relocate("io.sentry", "$prefix.io.sentry")
        relocate("org.bstats", "$prefix.org.bstats")
        relocate("io.github.slimjar", "$prefix.io.github.slimjar")
    }

//    ktlintFormat.alsoSubprojects()
    build.alsoSubprojects()
    clean.alsoSubprojects()

    withType<org.jetbrains.dokka.gradle.DokkaMultiModuleTask> {
        outputDirectory.set(File("$rootDir/docs"))
    }
}

allprojects {

    buildDir = file(rootProject.projectDir.resolve("build").resolve(project.name))

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
