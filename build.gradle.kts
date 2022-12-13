import com.google.devtools.ksp.gradle.KspGradleSubplugin
import dev.racci.paperweight.mpp.paperweightDevBundle
import kotlinx.validation.KotlinApiCompareTask
import net.minecrell.pluginyml.bukkit.BukkitPlugin
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder
import org.gradle.configurationcache.extensions.capitalized
import org.jetbrains.dokka.Platform
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin
import java.net.URL

val minixVersion: String by project
val version: String by project

// Workaround for (https://youtrack.jetbrains.com/issue/KTIJ-19369)
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("multiplatform") version "1.7.22"
    // FIXME -> Fix in Conventions
    // alias(libs.plugins.minix.kotlin)
    alias(libs.plugins.minix.copyjar) apply false
    alias(libs.plugins.minix.purpurmc) apply false

    alias(libs.plugins.ksp)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin.atomicfu) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.binaryValidator)

    alias(libs.plugins.shadow)
    alias(libs.plugins.slimjar)
    alias(libs.plugins.minecraft.pluginYML)
    alias(libs.plugins.minecraft.runPaper)
    id("dev.racci.paperweight.mpp")
}

apiValidation {
    ignoredProjects = subprojects.filter { it.name.contains("core") }.map(Project::getName).toMutableSet()

    ignoredPackages.add("dev.racci.minix.core")
    nonPublicMarkers.add("dev.racci.minix.api.MinixInternal")
}

runPaper.disablePluginJarDetection()

fun Project.emptySources() = project.sourceSets.none { set -> set.allSource.any { file -> file.extension == "kt" } }

fun SourceDirectorySet.maybeExtend(vararg objects: Any) {
    objects.map {
        when (it) {
            is File -> it
            is String -> project.file(it)
            else -> error("Unknown type: $it")
        }
    }.filter { it.exists() }.also { srcDirs(it) }
}

inline fun <reified T : Task> TaskContainer.apiTask(
    prefix: String?,
    suffix: String,
    configure: T.() -> Unit = {}
): T = buildString {
    if (prefix != null) append(prefix)
    if (prefix != null) append("Api") else append("api")
    append(suffix)
}.let<String, T>(::getByName).also(configure)

fun Project.maybeConfigureBinaryValidator(prefix: String? = null) {
    this.tasks {
        // For some reason this task likes to delete the entire folder contents,
        // So we need all projects to have their own sub folder.
        val name = if (prefix != null) "plugin-$prefix" else project.name
        val apiDir = file("$rootDir/config/api/${name.toLowerCase()}")
        apiTask<Sync>(prefix, "Dump") { destinationDir = apiDir }
        apiTask<KotlinApiCompareTask>(prefix, "Check") { projectApiDir = apiDir }
    }
}

tasks {
//    afterEvaluate {

//    }
//    val quickBuild by creating {
//        group = "build"
//        dependsOn(compileJava)
//        dependsOn(shadowJar)
// //        dependsOn(reobfJar)
//        findByName("copyJar")?.let { dependsOn(it) }
//    }

    runServer {
        val dependTask = kotlin.targets["paper"].compilations["main"].compileKotlinTask
        this.dependsOn(dependTask)
        this.minecraftVersion("1.19.2")
        this.pluginJars(dependTask.outputs.files.singleFile)
    }
}

allprojects {
    buildDir = file(rootProject.projectDir.resolve("build").resolve(project.name))

    configurations.whenObjectAdded {
        if (this.name == "testImplementation") {
            exclude("org.jetbrains.kotlin", "kotlin-test-junit")
        }

        // These refuse to resolve sometimes
        exclude("me.carleslc.Simple-YAML", "Simple-Configuration")
        exclude("me.carleslc.Simple-YAML", "Simple-Yaml")
        exclude("com.github.technove", "Flare")
    }

    afterEvaluate {

        // TODO: Disable unwanted modules
        kotlinExtension.apply {
            this.jvmToolchain(17)
            this.explicitApiWarning() // Koin generated sources aren't complicit.
            this.kotlinDaemonJvmArgs = listOf("-Xemit-jvm-type-annotations")

            sourceSets.forEach { set -> set.kotlin.maybeExtend("$buildDir/generated/ksp/main/kotlin") }
            (sourceSets.findByName("test") ?: sourceSets.getByName("commonTest")).apply { kotlin.maybeExtend("$buildDir/generated/ksp/main/kotlin") }
        }

        if (project.emptySources()) {
            tasks.apiDump.get().enabled = false
            tasks.apiCheck.get().enabled = false
        } else {
            apply<KtlintPlugin>()
            configure<KtlintExtension> {
                this.baseline.set(file("$rootDir/config/ktlint/baseline-${project.name}.xml"))
            }

            project.maybeConfigureBinaryValidator()
        }
    }

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://repo.racci.dev/releases/") {
            mavenContent {
                releasesOnly()
                includeGroupByRegex("dev.racci(\\.[a-z]+)?")
            }
        }

        maven("https://repo.racci.dev/snapshots/")

        maven("https://repo.fvdh.dev/releases") {
            mavenContent {
                releasesOnly()
                includeGroup("net.frankheijden.serverutils")
            }
        }

        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") {
            mavenContent {
                releasesOnly()
                includeModule("me.clip", "placeholderapi")
            }
        }
        maven("https://jitpack.io")
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.purpurmc.org/snapshots")
    }

    tasks {
        withType<Test>().configureEach {
            this.enabled = false
            useJUnitPlatform()
        }
    }
}

kotlin {
    fun KotlinSourceSet.setDirs(module: String, api: Boolean) {
        this.kotlin.srcDirs.clear()
        val prefix = buildString {
            append("minix-plugin/")
            if (api) append("api") else append("core")
            append('-').append(module)
            append("/src/main")
        }.let(::file)

        this.kotlin.maybeExtend(
            prefix.resolve("kotlin"),
            prefix.resolve("java")
        )

        this.resources.maybeExtend(prefix.resolve("resources"))
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
                api(project(":module-autoscanner"))
                api(project(":module-common"))
                api(project(":module-data"))
                api(project(":module-flowbus"))
                api(project(":module-integrations"))
//                api(project(":minix-modules:module-wrappers"))
                api(project(":module-ticker"))

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
            this.resources.srcDir(tasks.generateBukkitPluginDescription.get())

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
                paperweightDevBundle("org.purpurmc.purpur", project.properties["serverVersion"] as String)
            }
        }

        targets {
            fun KotlinJvmTarget.registerQuickBuild() {
                this.project.rootProject.tasks.register("${this.name.toLowerCase().capitalized()}QuickBuild") {
                    this.group = "build"
                    this.dependsOn(this@registerQuickBuild.compilations.getByName("main").compileKotlinTask)
                }
            }

            jvm("paper") {
                this.project.maybeConfigureBinaryValidator(this.name)
                this.project.apply<BukkitPlugin>()
                this.project.bukkit {
                    this.name = "Minix"
                    this.prefix = "Minix"
                    this.author = "Racci"
                    this.apiVersion = "1.19"
                    this.version = rootProject.version.toString()
                    this.main = "dev.racci.minix.core.MinixInit"
                    this.load = PluginLoadOrder.STARTUP
                    this.loadBefore = listOf("eco")
                    this.softDepend = listOf("PlaceholderAPI", "Lands", "ServerUtils")
                    this.website = "https://github.com/DaRacci/Minix"
                }
            }
        }
    }
}.also { // Order matters
    dependencies {
        add("kspCommonMainMetadata", "io.insert-koin:koin-ksp-compiler:1.0.3")
        add("kspPaper", "io.insert-koin:koin-ksp-compiler:1.0.3")
    }
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
        val common = project(":module-common")
        if (this@subprojects.project !== common.dependencyProject) {
            compileOnly(common)
            testImplementation(common)
        }

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
    val targets = project.kotlin.targets.mapNotNull { it.project.tasks.findByName("${it.name}${name.capitalized()}") }
    val subprojects = subprojects.mapNotNull { it.tasks.findByName(name) }
    val builds = gradle.includedBuilds.mapNotNull { it.task(":$name") }
    this {
        dependsOn(targets + subprojects + builds)
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
        relocate("io.sentry", "$prefix.sentry") // TODO -> Slimjar Relocate
        relocate("org.bstats", "$prefix.bstats") // TODO -> Slimjar Relocate
        relocate("io.github.slimjar", "$prefix.slimjar")
        relocate("org.koin.ksp.generated", "$prefix.generated.koin")
    }

//    ktlintFormat.alsoSubprojects()
    build.alsoSubprojects()
    clean.alsoSubprojects()

    withType<org.jetbrains.dokka.gradle.DokkaMultiModuleTask> {
        outputDirectory.set(File("$rootDir/docs"))
    }
}
