package dev.racci.paperweight.mpp

import io.papermc.paperweight.tasks.RemapJar
import org.gradle.api.Project
import org.gradle.api.UnknownTaskException
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.named
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

/**
 * Configures input/output for `reobfJar` and potentially changes classifiers of other jars.
 */
public fun interface ReobfArtifactConfigurationMPP {
    public fun configure(
        target: KotlinJvmTarget,
        reobfJar: TaskProvider<RemapJar>
    )

    public companion object {
        /**
         * Used when the reobfJar artifact is the main production output.
         *
         * Sets the `jar` classifier to `dev`, and the `shadowJar` classifier to `dev-all` if it exists.
         * The `reobfJar` will have no classifier. [BasePluginExtension.getArchivesName] is used to name
         * the `reobfJar`, falling back to the project name if it is not configured.
         */
        public val REOBF_PRODUCTION: ReobfArtifactConfigurationMPP = ReobfArtifactConfigurationMPP { target, reobfJar ->
            val jar = target.project.tasks.named<AbstractArchiveTask>(target.artifactsTaskName) {
                archiveClassifier.set("dev")
            }

            val devJarTask = try {
                target.project.tasks.named<AbstractArchiveTask>(target.disambiguateName("shadowJar")) {
                    archiveClassifier.set("dev-all")
                }
            } catch (ex: UnknownTaskException) {
                jar
            }

            reobfJar.configure {
                inputJar.set(devJarTask.flatMap { it.archiveFile })
                outputJar.convention(archivesName(project).flatMap { layout.buildDirectory.file("libs/${target.project.name}-${target.name}-${project.version}.jar") })
            }
        }

        /**
         * Used when the Mojang-mapped artifact (`jar`/`shadowJar`) is the main production output.
         *
         * Does not modify `jar` or `shadowJar` classifier, [BasePluginExtension.getArchivesName] is used to name
         * the `reobfJar`, falling back to the project name if it is not configured.
         */
        public val MOJANG_PRODUCTION: ReobfArtifactConfigurationMPP = ReobfArtifactConfigurationMPP { target, reobfJar ->
            val devJarTask = try {
                target.project.tasks.named<AbstractArchiveTask>(target.disambiguateName("shadowJar"))
            } catch (ex: UnknownTaskException) {
                target.project.tasks.named<AbstractArchiveTask>(target.artifactsTaskName)
            }

            reobfJar.configure {
                inputJar.set(devJarTask.flatMap { it.archiveFile })
                outputJar.convention(archivesName(project).flatMap { layout.buildDirectory.file("libs/$it-${project.version}-reobf.jar") })
            }
        }

        private fun archivesName(project: Project): Provider<String> = project.extensions.findByType(BasePluginExtension::class)?.archivesName ?: project.provider { project.name }
    }
}
