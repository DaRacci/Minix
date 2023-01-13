import io.gitlab.arturbosch.detekt.DetektPlugin
import kotlinx.kover.KoverPlugin
import org.jetbrains.dokka.gradle.DokkaPlugin

plugins {
    id(libs.plugins.minix.get().pluginId)
}

subprojects {
    apply<JavaLibraryPlugin>()
    apply(plugin = rootProject.libs.plugins.kotlin.jvm.get().pluginId)
    apply<KoverPlugin>()
    apply<DokkaPlugin>()
//    apply<DetektPlugin>()

    configurations {
        val compileAndTest by creating {
            get("compileClasspath").extendsFrom(this)
            get("testImplementation").extendsFrom(this)
        }
        register("compileAndTestApi") {
            get("compileOnlyApi").extendsFrom(this)
            compileAndTest.extendsFrom(this)
        }
    }

    dependencies {
        val common = project(":minix-modules:module-common")
        if (this@subprojects.project !== common.dependencyProject) {
            "compileOnly"(common)
            "testImplementation"(common)
        }

        @Suppress("UnstableApiUsage")
        "testImplementation"(rootProject.libs.testing.kotest.junit5)
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
