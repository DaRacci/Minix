import com.google.devtools.ksp.gradle.KspGradleSubplugin
import kotlinx.validation.KotlinApiCompareTask
import net.minecrell.pluginyml.bukkit.BukkitPlugin
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder
import org.gradle.configurationcache.extensions.capitalized
import org.jetbrains.dokka.Platform
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin
import java.net.URL

val minixVersion: String by project
val version: String by project

// Workaround for (https://youtrack.jetbrains.com/issue/KTIJ-19369)
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("multiplatform") version "1.7.21"
    // FIXME -> Fix in Conventions
    // alias(libs.plugins.minix.kotlin)
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
    ignoredProjects = subprojects.filter { it.name.contains("core") }.map(Project::getName).toMutableSet()
    ignoredPackages += "minix-modules"

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
    fun KotlinSourceSet.setDirs(module: String, api: Boolean) {
        this.kotlin.srcDirs.clear()

        if (api) {
            this.kotlin.setSrcDirs(listOf("minix-plugin/api-$module/src/main/kotlin"))
        } else {
            this.kotlin.setSrcDirs(listOf("minix-plugin/core-$module/src/main/kotlin"))
        }
    }

    sourceSets {
        val commonAPI by creating {
            setDirs("common", true)

            this.dependencies {
                api(libs.kotlin.stdlib)
                api(libs.kotlin.reflect)
                api(libs.kotlinx.dateTime)
                api(libs.kotlinx.atomicfu)
                api(libs.kotlinx.coroutines)
                api(libs.kotlinx.immutableCollections)

                api(libs.arrow.core)
                api(libs.arrow.optics)
                api(libs.arrow.fx.coroutines)
                api(libs.arrow.optics.reflect)
                api(libs.arrow.laws)
            }
        }

        val commonMain by getting {
            this.setDirs("common", false)
            this.dependsOn(commonAPI)

            dependencies {
                api(project(":minix-modules:module-autoscanner"))
                api(project(":minix-modules:module-common"))
                api(project(":minix-modules:module-data"))
                api(project(":minix-modules:module-flowbus"))
                api(project(":minix-modules:module-integrations"))
                api(project(":minix-modules:module-wrappers"))
                api(project(":minix-modules:module-ticker"))

                api(libs.koin.core)
                api(libs.mordant)
                api(libs.caffeine)
                api(libs.aedile)
                api(libs.minecraft.bstats.base)
                api(libs.toolfactory)
                api(libs.slimjar)
                api(libs.mariadb)

                api(libs.sentry.core)
                api(libs.sentry.kotlin)

                api(libs.hikariCP)
                api(libs.adventure.configurate)

                // Annotations
                compileOnly(libs.apiguardian)
                compileOnly("io.insert-koin:koin-annotations:1.0.3")
            }
        }

        val paperAPI by creating {
            this.setDirs("paper", true)

            this.dependsOn(commonAPI)

            this.dependencies {
                compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")

                // Integrations
                compileOnly(libs.minecraft.api.landsAPI)
                compileOnly(libs.minecraft.api.placeholderAPI)
            }
        }

        val paperMain by creating {

            this.setDirs("paper", false)
            this.kotlin.srcDir("minix-plugin/core-paper/src/main/java")

            this.dependsOn(commonMain)
            this.dependsOn(paperAPI)

            this.dependencies {
                // Integrations
                compileOnly(libs.minecraft.api.serverutils)

                // MinixLogger backend hooks for paper logger
                compileOnly("org.apache.logging.log4j:log4j-core:2.19.0")
                compileOnly("net.minecrell:terminalconsoleappender:1.3.0")
                compileOnly("org.jline:jline-terminal-jansi:3.21.0")

                api(libs.minecraft.bstats.bukkit)
                api(libs.configurate.hocon)
                api(libs.configurate.extra.kotlin)
            }
        }
    }

    targets {
        jvm("paper") {
            this.withJava()

            this.project.apply<Dev_racci_minix_nmsPlugin>()
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
    }
}

dependencies {
    add("kspCommonMainMetadata", "io.insert-koin:koin-ksp-compiler:1.0.3")
    add("kspPaper", "io.insert-koin:koin-ksp-compiler:1.0.3")
}

subprojects {
    apply<DokkaPlugin>()
    apply<JavaLibraryPlugin>()
    apply<MavenPublishPlugin>()
    apply<KotlinPlatformJvmPlugin>()
    apply<KspGradleSubplugin>()

    configurations {
        val slim by creating

        compileClasspath.get().extendsFrom(slim)
        runtimeClasspath.get().extendsFrom(slim)
        apiElements.get().extendsFrom(slim)
    }

    dependencies {
        val common = project(":minix-modules:module-common")
        if (this@subprojects.project !== common.dependencyProject) {
            compileOnly(common)
            testImplementation(common)
        }

        ksp("io.arrow-kt:arrow-meta:1.6.1-SNAPSHOT")
        ksp("io.insert-koin:koin-ksp-compiler:1.0.3")
        ksp(rootProject.libs.arrow.optics.ksp)
    }

    tasks {

        dokkaHtml.get().dokkaSourceSets.configureEach {
            includeNonPublic.set(false)
            skipEmptyPackages.set(true)
            reportUndocumented.set(true)
            displayName.set(project.name.split("-")[1])
            platform.set(Platform.jvm)
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
        subSlim.dependencies.forEach { dep -> rootProject.dependencies { slim(dep) } }
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
                it.moduleGroup == libs.minecraft.bstats.base.get().module.group ||
                it.moduleGroup == "dev.racci.slimjar"
        }
        val prefix = "dev.racci.minix.libs"
        relocate("io.sentry", "$prefix.io.sentry") // TODO -> Slimjar Relocate
        relocate("org.bstats", "$prefix.org.bstats") // TODO -> Slimjar Relocate
        relocate("io.github.slimjar", "$prefix.io.github.slimjar")
    }

//    ktlintFormat.alsoSubprojects()
    build.alsoSubprojects()
    clean.alsoSubprojects()

    withType<org.jetbrains.dokka.gradle.DokkaMultiModuleTask> {
        outputDirectory.set(File("$rootDir/docs"))
    }
}

fun Project.emptySources() = project.sourceSets.none { set -> set.allSource.any { file -> file.extension == "kt" } }

allprojects {
    if (!project.emptySources()) {
        apply<KtlintPlugin>()

        configure<KtlintExtension> {
            this.baseline.set(file("$rootDir/config/ktlint/baseline-${project.name}.xml"))
        }

        tasks {
            // For some reason this task likes to delete the entire folder contents,
            // So we need all projects to have their own sub folder.
            val apiDir = file("$rootDir/config/api/${project.name.toLowerCase()}")
            (apiDump as TaskProvider<Sync>) { destinationDir = apiDir }
            (apiCheck as TaskProvider<KotlinApiCompareTask>) { projectApiDir = apiDir }
        }
    } else {
        tasks {
            apiDump.get().enabled = false
            apiCheck.get().enabled = false
        }
    }

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

    tasks {
        withType<Test>().configureEach {
            this.enabled = false
            useJUnitPlatform()
        }

        withType<KotlinCompile>().configureEach {
            kotlinOptions.suppressWarnings = true
        }
    }

    kotlinExtension.apply {
        this.jvmToolchain(17)
        this.explicitApiWarning() // Koin generated sources aren't complicit.
        this.kotlinDaemonJvmArgs = listOf("-Xemit-jvm-type-annotations")

        sourceSets.forEach {
            it.kotlin.srcDir("$buildDir/generated/ksp/main/kotlin")
        }
//        (sourceSets.findByName("main") ?: sourceSets.getByName("commonMain")).apply {
//            this.kotlin.srcDir("$buildDir/generated/ksp/main/kotlin")
//        }
        (sourceSets.findByName("test") ?: sourceSets.getByName("commonTest")).apply {
            this.kotlin.srcDir("$buildDir/generated/ksp/test/kotlin")
        }
    }
}
