package dev.racci.paperweight.mpp

import io.papermc.paperweight.tasks.* // ktlint-disable no-wildcard-imports
import io.papermc.paperweight.userdev.internal.setup.ExtractedBundle
import io.papermc.paperweight.userdev.internal.setup.RunPaperclip
import io.papermc.paperweight.userdev.internal.setup.SetupHandler
import io.papermc.paperweight.userdev.internal.setup.step.* // ktlint-disable no-wildcard-imports
import io.papermc.paperweight.userdev.internal.setup.util.HashFunctionBuilder
import io.papermc.paperweight.util.* // ktlint-disable no-wildcard-imports
import io.papermc.paperweight.util.constants.* // ktlint-disable no-wildcard-imports
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.DependencySet
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.repositories.IvyArtifactRepository
import java.nio.file.Path

class SetupHandlerImplMPP(
    private val project: Project,
    private val service: UserdevSetupMPP,
    private val bundle: ExtractedBundle<GenerateDevBundle.DevBundleConfig>,
    private val cache: Path = service.parameters.cache.path
) : SetupHandlerMPP {
    private val vanillaSteps by lazy {
        VanillaSteps(
            bundle.config.minecraftVersion,
            cache,
            service.parameters.downloadService.get(),
            bundle.changed
        )
    }
    private val vanillaServerJar: Path = cache.resolve(paperSetupOutput("vanillaServerJar", "jar"))
    private val minecraftLibraryJars = cache.resolve(MINECRAFT_JARS_PATH)
    private val filteredVanillaServerJar: Path = cache.resolve(paperSetupOutput("filterJar", "jar"))
    private val mojangPlusYarnMappings: Path = cache.resolve(MOJANG_YARN_MAPPINGS)
    private val mappedMinecraftServerJar: Path = cache.resolve(paperSetupOutput("mappedMinecraftServerJar", "jar"))
    private val fixedMinecraftServerJar: Path = cache.resolve(paperSetupOutput("fixedMinecraftServerJar", "jar"))
    private val accessTransformedServerJar: Path = cache.resolve(paperSetupOutput("accessTransformedServerJar", "jar"))
    private val decompiledMinecraftServerJar: Path = cache.resolve(paperSetupOutput("decompileMinecraftServerJar", "jar"))
    private val patchedSourcesJar: Path = cache.resolve(paperSetupOutput("patchedSourcesJar", "jar"))
    private val mojangMappedPaperJar: Path = cache.resolve(paperSetupOutput("applyMojangMappedPaperclipPatch", "jar"))

    private fun minecraftLibraryJars(): List<Path> = minecraftLibraryJars.filesMatchingRecursive("*.jar")

    private fun generateSources(context: SetupHandlerMPP.Context) {
        vanillaSteps.downloadVanillaServerJar()

        val extractStep = createExtractFromBundlerStep()

        val filterVanillaJarStep = FilterVanillaJar(vanillaServerJar, bundle.config.buildData.vanillaJarIncludes, filteredVanillaServerJar)

        // Start - GenerateMappingsStep.create
        vanillaSteps.downloadServerMappings()
        val paramMappings = context.project.configurations.named(context.source.prefixedName(PARAM_MAPPINGS_CONFIG)).map { it.singleFile }.convertToPath()
        val genMappingsStep = GenerateMappingsStep(
            vanillaSteps,
            filteredVanillaServerJar,
            paramMappings,
            ::minecraftLibraryJars,
            mojangPlusYarnMappings
        )
        // End - GenerateMappingsStep.create

        // Start - RemapMinecraft.create
        val remapper = context.project.configurations.getByName(context.source.prefixedName(REMAPPER_CONFIG)).also(Configuration::resolve) // resolve remapper
        val remapMinecraftStep = RemapMinecraft(
            bundle.config.buildData.minecraftRemapArgs,
            filteredVanillaServerJar,
            ::minecraftLibraryJars,
            mojangPlusYarnMappings,
            remapper,
            mappedMinecraftServerJar,
            cache
        )
        // End - RemapMinecraft.create

        val fixStep = FixMinecraftJar(
            mappedMinecraftServerJar,
            fixedMinecraftServerJar,
            vanillaServerJar
        )

        val atStep = AccessTransformMinecraft(
            bundle.dir.resolve(bundle.config.buildData.accessTransformFile),
            fixedMinecraftServerJar,
            accessTransformedServerJar
        )

        // Start - DecompileMinecraft.create
        val decompiler = context.project.configurations.getByName(context.source.prefixedName(DECOMPILER_CONFIG)).also(Configuration::resolve) // resolve decompiler
        val decomp = DecompileMinecraft(
            accessTransformedServerJar,
            decompiledMinecraftServerJar,
            cache,
            ::minecraftLibraryJars,
            bundle.config.decompile.args,
            decompiler
        )
        // End - DecompileMinecraft.create

        val applyDevBundlePatchesStep = ApplyDevBundlePatches(
            decompiledMinecraftServerJar,
            bundle.dir.resolve(bundle.config.patchDir),
            patchedSourcesJar
        )

        StepExecutor.executeSteps(
            context.stdContext,
            extractStep,
            filterVanillaJarStep,
            genMappingsStep,
            remapMinecraftStep,
            fixStep,
            atStep,
            decomp,
            applyDevBundlePatchesStep
        )
    }

    // This can be called when a user queries the server jar provider in
    // PaperweightUserExtension, possibly by a task running in a separate
    // thread to dependency resolution.
    @Synchronized
    private fun applyMojangMappedPaperclipPatch(context: SetupHandlerMPP.Context) {
        if (setupCompleted) {
            return
        }

        StepExecutor.executeStep(
            context.stdContext,
            RunPaperclip(
                bundle.dir.resolve(bundle.config.buildData.mojangMappedPaperclipFile),
                mojangMappedPaperJar,
                vanillaSteps.mojangJar,
                minecraftVersion
            )
        )
    }

    private var setupCompleted = false

    @Synchronized
    override fun createOrUpdateIvyRepository(context: SetupHandlerMPP.Context) {
        if (setupCompleted) {
            return
        }

        val source = if (service.parameters.genSources.get()) {
            generateSources(context)
            patchedSourcesJar
        } else {
            vanillaSteps.downloadVanillaServerJar()
            StepExecutor.executeStep(context.stdContext, createExtractFromBundlerStep())
            null
        }

        applyMojangMappedPaperclipPatch(context)

        val deps = bundle.config.buildData.compileDependencies.toList() +
            bundle.config.apiCoordinates +
            bundle.config.mojangApiCoordinates
        installPaperServer(
            cache,
            bundle.config.mappedServerCoordinates,
            deps,
            mojangMappedPaperJar,
            source,
            minecraftVersion
        )

        setupCompleted = true
    }

    override fun configureIvyRepo(repo: IvyArtifactRepository) {
        repo.content {
            includeFromDependencyNotation(bundle.config.mappedServerCoordinates)
        }
    }

    override fun populateCompileConfiguration(context: SetupHandlerMPP.Context, dependencySet: DependencySet) {
        dependencySet.add(context.project.dependencies.create(bundle.config.mappedServerCoordinates))
    }

    override fun populateRuntimeConfiguration(context: SetupHandlerMPP.Context, dependencySet: DependencySet) {
        listOf(
            bundle.config.mappedServerCoordinates,
            bundle.config.apiCoordinates,
            bundle.config.mojangApiCoordinates
        ).forEach { coordinate ->
            val dep = context.project.dependencies.create(coordinate).also {
                (it as ExternalModuleDependency).isTransitive = false
            }
            dependencySet.add(dep)
        }

        for (coordinates in bundle.config.buildData.runtimeDependencies) {
            val dep = context.project.dependencies.create(coordinates).also {
                (it as ExternalModuleDependency).isTransitive = false
            }
            dependencySet.add(dep)
        }
    }

    override fun serverJar(context: SetupHandlerMPP.Context): Path {
        applyMojangMappedPaperclipPatch(context)
        return mojangMappedPaperJar
    }

    override val serverJar: Path
        get() = mojangMappedPaperJar

    override val reobfMappings: Path
        get() = bundle.dir.resolve(bundle.config.buildData.reobfMappingsFile)

    override val minecraftVersion: String
        get() = bundle.config.minecraftVersion

    override val pluginRemapArgs: List<String>
        get() = bundle.config.buildData.pluginRemapArgs

    override val paramMappings: MavenDep
        get() = bundle.config.buildData.paramMappings

    override val decompiler: MavenDep
        get() = bundle.config.decompile.dep

    override val remapper: MavenDep
        get() = bundle.config.remapper

    override val libraryRepositories: List<String>
        get() = bundle.config.buildData.libraryRepositories

    private fun createExtractFromBundlerStep(): ExtractFromBundlerStep = ExtractFromBundlerStep(
        cache,
        vanillaSteps,
        vanillaServerJar,
        minecraftLibraryJars,
        ::minecraftLibraryJars
    )

    private class ExtractFromBundlerStep(
        cache: Path,
        private val vanillaSteps: VanillaSteps,
        private val vanillaServerJar: Path,
        private val minecraftLibraryJars: Path,
        private val listMinecraftLibraryJars: () -> List<Path>
    ) : SetupStep {
        override val name: String = "extract libraries and server from downloaded jar"

        override val hashFile: Path = cache.resolve(paperSetupOutput("extractFromServerBundler", "hashes"))

        override fun run(context: SetupHandler.Context) {
            ServerBundler.extractFromBundler(
                vanillaSteps.mojangJar,
                vanillaServerJar,
                minecraftLibraryJars,
                null,
                null,
                null,
                null
            )
        }

        override fun touchHashFunctionBuilder(builder: HashFunctionBuilder) {
            builder.include(vanillaSteps.mojangJar, vanillaServerJar)
            builder.include(listMinecraftLibraryJars())
        }
    }
}
