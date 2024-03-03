import dev.racci.minix.gradle.data.MCTarget
import dev.racci.minix.gradle.support.KotlinJvmPluginSupport.kotlin
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import kotlinx.validation.KotlinApiBuildTask
import kotlinx.validation.KotlinApiCompareTask

plugins {
    id(libs.plugins.minix.get().pluginId) version "0.5.3"

    id("org.jetbrains.kotlinx.kover") version "0.6.1" // TODO: Catalog and convention
    id("io.gitlab.arturbosch.detekt") version "1.22.0" // TODO: Catalog and convention

    alias(libs.plugins.kotlin.plugin.dokka)
    alias(libs.plugins.kotlin.plugin.binaryValidator)

    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.mpp) apply false
    alias(libs.plugins.shadow) apply false
    alias(libs.plugins.kotlin.plugin.atomicfu) apply false
    alias(libs.plugins.kotlin.plugin.serialization) apply false
    id(libs.plugins.slimjar.get().pluginId) version "2.0.0-SNAPSHOT" apply false
}

koverMerged {
    enable()
    filters {
        projects {
            excludes += "minix-plugin"
            excludes += "minix-modules"
        }
        classes {
            includes += "dev.racci.minix"
        }
    }
    htmlReport {
        onCheck.set(false)
        reportDir.set(layout.buildDirectory.dir("reports/kover/html"))
    }
}

apiValidation {
    ignoredProjects = subprojects.filter { it.name.contains("core") }.map(Project::getName).toMutableSet()

    ignoredPackages.add("dev.racci.minix.core")
    nonPublicMarkers.add("dev.racci.minix.api.MinixInternal")
}

minix {
    ignoredTargets += "minix-plugin"
    ignoredTargets += "module-common"
    minecraft {
        fun target(
            project: String,
            platform: MCTarget.Platform = MCTarget.Platform.PAPER
        ) = withMCTarget(project(":minix-modules:module-$project"), platform, applyMinix = false)
        target("integrations")
        target("jumper")
        target("data", MCTarget.Platform.PURPUR)
    }
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.22.0")
}

val reportMerge by tasks.creating(ReportMergeTask::class) {
    output.set(rootProject.layout.buildDirectory.file("reports/detekt/detekt.sarif"))
}

subprojects {
    extensions.findByType<DetektExtension>()?.apply {
        toolVersion = "1.22.0"
        config = rootProject.files("config/detekt/detekt.yml")
        buildUponDefaultConfig = true
        parallel = true
        basePath = rootProject.projectDir.path
    }

    tasks.withType<Detekt> detekt@{
        reportMerge.input.from(this@detekt.sarifReportFile)
        finalizedBy(reportMerge)
        reports {
            md.required.set(false)
            html.required.set(false)
            txt.required.set(false)
            xml.required.set(false)
            sarif {
                required.set(true)
                outputLocation.set(rootProject.buildDir.resolve("reports/detekt/detekt-${project.name}.sarif"))
            }
        }
    }

//    tasks {
//        // For some reason this task likes to delete the entire folder contents,
//        // So we need all projects to have their own sub folder.
//        val name = if (prefix != null) "plugin-$prefix" else project.name
//        val subDir = "api/${name.toLowerCase()}"
//        val apiDir = rootProject.layout.projectDirectory.file("config/$subDir").asFile
//        apiTask<KotlinApiBuildTask>(prefix, "Build") {
//            outputApiDir = rootProject.buildDir.resolve("api/$subDir")
//        }
//        apiTask<Sync>(prefix, "Dump") { destinationDir = apiDir }
//        apiTask<KotlinApiCompareTask>(prefix, "Check") { projectApiDir = apiDir }
//    }

    // For some reason these tasks like to delete the entire folder contents,
    // So we need all projects to have their own sub folder.
    afterEvaluate {
        if (tasks.findByName("apiCheck") == null) return@afterEvaluate

        fun Task.dirName(): String = buildString {
            if (!name.startsWith("api")) append(name.substringBefore("Api")).append('/')
            append(project.name)
        }

        tasks.withType<KotlinApiBuildTask> {
            outputApiDir = rootProject.buildDir.resolve("api").resolve(dirName())
        }
        tasks.withType<Sync> {
            if (!name.endsWith("piDump")) return@withType
            destinationDir = rootProject.layout.projectDirectory.file("config/api/${dirName()}").asFile
        }
        tasks.withType<KotlinApiCompareTask> {
            projectApiDir = rootProject.projectDir.resolve("config/api").resolve(dirName())
        }
    }
}
