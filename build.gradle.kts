import com.google.devtools.ksp.gradle.KspGradleSubplugin
import net.minecrell.pluginyml.bukkit.BukkitPlugin
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URL

val minixVersion: String by project
val version: String by project

// Workaround for (https://youtrack.jetbrains.com/issue/KTIJ-19369)
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("multiplatform") version "1.7.20"
// TODO -> FIX
//  alias(libs.plugins.minix.kotlin)
    alias(libs.plugins.minix.copyjar) apply false
    alias(libs.plugins.minix.purpurmc) apply false

    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin.atomicfu) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.binaryValidator)

    alias(libs.plugins.shadow)
    alias(libs.plugins.slimjar)
    alias(libs.plugins.minecraft.pluginYML)

    id("com.google.devtools.ksp") version "1.7.20-1.0.7"
}

apiValidation {
//    ignoredProjects = subprojects.first { it.name.contains("core") }.subprojects.map(Project::getName).toMutableSet()

    ignoredPackages.add("dev.racci.minix.core")
    nonPublicMarkers.add("dev.racci.minix.api.MinixInternal")
}

tasks {
    val quickBuild by creating {
        group = "build"
        dependsOn(compileJava)
        dependsOn(shadowJar)
//        dependsOn(reobfJar)
        findByName("copyJar")?.let { dependsOn(it) }
    }
}

kotlin {
    explicitApiWarning()
    kotlinDaemonJvmArgs = listOf("-Xemit-jvm-type-annotations")

    fun KotlinSourceSet.setDirs(module: String) {
        this.kotlin.srcDirs.clear()
        this.kotlin.setSrcDirs(
            listOf(
                "minix-plugin/api-$module/src/main/kotlin",
                "minix-plugin/core-$module/src/main/kotlin"
            )
        )
    }

    sourceSets {
        val commonMain by getting {
            this.setDirs("common")

            dependencies {
                api(project(":minix-modules:module-autoscanner"))
                api(project(":minix-modules:module-common"))
                api(project(":minix-modules:module-data"))
                api(project(":minix-modules:module-eventbus"))
                api(project(":minix-modules:module-integrations"))
//                api(project(":minix-modules:module-updater"))
                api(project(":minix-modules:module-wrappers"))

                api(libs.kotlin.stdlib)
                api(libs.kotlin.reflect)
                api(libs.kotlinx.dateTime)
                api(libs.kotlinx.atomicfu)
                api(libs.kotlinx.coroutines)
                api(libs.kotlinx.immutableCollections)

                api(libs.koin.core)
                api(libs.mordant)
                api(libs.caffeine)
                api("org.bstats:bstats-base:3.0.0")
                api("io.github.toolfactory:jvm-driver:9.3.0")
                api("dev.racci.slimjar:slimjar:1.2.11")

                api(libs.sentry.core)
                api(libs.sentry.kotlin)

                api(libs.hikariCP)

                // Annotations
                compileOnly("org.apiguardian:apiguardian-api:1.1.2")
                compileOnly("io.insert-koin:koin-annotations:1.0.3")
            }
        }

        val paperMain by creating {
            this.setDirs("paper")
            this.kotlin.srcDir("minix-plugin/core-paper/src/main/java")
            this.dependsOn(commonMain)

            this.dependencies {
                compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
                compileOnly(libs.minecraft.api.landsAPI)
                compileOnly(libs.minecraft.api.placeholderAPI)
                compileOnly("net.frankheijden.serverutils:ServerUtils:3.5.3")

                api(libs.minecraft.bstats)
            }
        }

        val velocityMain by creating {
            this.setDirs("velocity")
            this.dependsOn(commonMain)

            this.dependencies {
                compileOnly("com.velocitypowered:velocity-api:3.1.1")
                api("org.bstats:bstats-velocity:3.0.0")
            }
        }
    }

    targets {
        all {
            compilations.all {
                kotlinOptions {
                    this.allWarningsAsErrors = true
                    this.freeCompilerArgs += "-opt-in=dev.racci.minix.api.annotations.MinixInternal"
                }
            }
        }

        jvm("paper") {
            this.project.apply<BukkitPlugin>()
            this.project.bukkit {
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
        }
        jvm("velocity")
    }
}

dependencies {
    add("kspCommonMainMetadata", "io.insert-koin:koin-ksp-compiler:1.0.3")
    add("kspVelocity", "com.velocitypowered:velocity-api:3.1.1")
    add("kspVelocity", "io.insert-koin:koin-ksp-compiler:1.0.3")
    add("kspPaper", "io.insert-koin:koin-ksp-compiler:1.0.3")
}

subprojects {
    apply<DokkaPlugin>()
    apply<JavaLibraryPlugin>()
    apply<MavenPublishPlugin>()
    apply<Dev_racci_minix_kotlinPlugin>()
    apply<KspGradleSubplugin>()

    configurations {
        val slim by creating

        compileClasspath.get().extendsFrom(slim)
        runtimeClasspath.get().extendsFrom(slim)
        apiElements.get().extendsFrom(slim)
    }

    (this as ExtensionAware).extensions.configure<KotlinJvmProjectExtension>("kotlin") {
        explicitApiWarning()
        kotlinDaemonJvmArgs = listOf("-Xemit-jvm-type-annotations")
    }

    dependencies {
        val common = project(":minix-modules:module-common")
        if (this@subprojects.project !== common.dependencyProject) {
            compileOnly(common)
        }

        ksp("io.arrow-kt:arrow-meta:1.6.1-SNAPSHOT")
        ksp("io.insert-koin:koin-ksp-compiler:1.0.3")
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

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://repo.fvdh.dev/releases")
        maven("https://repo.racci.dev/releases")
        maven("https://repo.purpurmc.org/snapshots")
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }

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
