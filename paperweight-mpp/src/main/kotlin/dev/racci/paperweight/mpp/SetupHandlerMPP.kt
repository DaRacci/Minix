package dev.racci.paperweight.mpp

import io.papermc.paperweight.PaperweightException
import io.papermc.paperweight.tasks.GenerateDevBundle
import io.papermc.paperweight.userdev.internal.setup.ExtractedBundle
import io.papermc.paperweight.userdev.internal.setup.SetupHandler
import io.papermc.paperweight.userdev.internal.setup.UserdevSetup
import io.papermc.paperweight.util.MavenDep
import io.papermc.paperweight.util.defaultJavaLauncher
import org.gradle.api.Project
import org.gradle.api.artifacts.DependencySet
import org.gradle.api.artifacts.repositories.IvyArtifactRepository
import org.gradle.jvm.toolchain.JavaLauncher
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.workers.WorkerExecutor
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import java.nio.file.Path

public interface SetupHandlerMPP {
    public fun createOrUpdateIvyRepository(context: Context)

    public fun configureIvyRepo(repo: IvyArtifactRepository)

    public fun populateCompileConfiguration(
        context: Context,
        dependencySet: DependencySet
    )

    public fun populateRuntimeConfiguration(
        context: Context,
        dependencySet: DependencySet
    )

    public fun serverJar(context: Context): Path

    public val serverJar: Path

    public val reobfMappings: Path

    public val minecraftVersion: String

    public val pluginRemapArgs: List<String>

    public val paramMappings: MavenDep

    public val decompiler: MavenDep

    public val remapper: MavenDep

    public val libraryRepositories: List<String>

    public data class Context(
        val project: Project,
        val source: KotlinSourceSet,
        val workerExecutor: WorkerExecutor,
        val javaToolchainService: JavaToolchainService
    ) {
        val defaultJavaLauncher: JavaLauncher
            get() = javaToolchainService.defaultJavaLauncher(project).get()

        val stdContext: SetupHandler.Context by lazy { SetupHandler.Context(project, workerExecutor, javaToolchainService) }
    }

    public companion object {
        @Suppress("unchecked_cast")
        public fun create(
            parameters: UserdevSetup.Parameters,
            extractedBundle: ExtractedBundle<Any>
        ): SetupHandlerMPP = when (extractedBundle.config) {
            is GenerateDevBundle.DevBundleConfig -> SetupHandlerImplMPP(
                parameters,
                extractedBundle as ExtractedBundle<GenerateDevBundle.DevBundleConfig>
            )
            else -> throw PaperweightException("Unknown dev bundle config type: ${extractedBundle.config::class.java.typeName}")
        }
    }
}
