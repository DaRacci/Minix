package dev.racci.paperweight.mpp

import io.papermc.paperweight.PaperweightException
import io.papermc.paperweight.tasks.GenerateDevBundle
import io.papermc.paperweight.userdev.internal.setup.ExtractedBundle
import io.papermc.paperweight.userdev.internal.setup.SetupHandler
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

interface SetupHandlerMPP {
    fun createOrUpdateIvyRepository(context: Context)

    fun configureIvyRepo(repo: IvyArtifactRepository)

    fun populateCompileConfiguration(context: Context, dependencySet: DependencySet)

    fun populateRuntimeConfiguration(context: Context, dependencySet: DependencySet)

    fun serverJar(context: Context): Path

    val serverJar: Path

    val reobfMappings: Path

    val minecraftVersion: String

    val pluginRemapArgs: List<String>

    val paramMappings: MavenDep

    val decompiler: MavenDep

    val remapper: MavenDep

    val libraryRepositories: List<String>

    data class Context(
        val project: Project,
        val source: KotlinSourceSet,
        val workerExecutor: WorkerExecutor,
        val javaToolchainService: JavaToolchainService
    ) {
        val defaultJavaLauncher: JavaLauncher
            get() = javaToolchainService.defaultJavaLauncher(project).get()

        val stdContext by lazy { SetupHandler.Context(project, workerExecutor, javaToolchainService) }
    }

    companion object {
        @Suppress("unchecked_cast")
        fun create(
            setupService: UserdevSetupMPP,
            extractedBundle: ExtractedBundle<Any>
        ): SetupHandlerMPP = when (extractedBundle.config) {
            is GenerateDevBundle.DevBundleConfig -> SetupHandlerImplMPP(
                setupService.cachedProject,
                setupService,
                extractedBundle as ExtractedBundle<GenerateDevBundle.DevBundleConfig>
            )
            else -> throw PaperweightException("Unknown dev bundle config type: ${extractedBundle.config::class.java.typeName}")
        }
    }
}
