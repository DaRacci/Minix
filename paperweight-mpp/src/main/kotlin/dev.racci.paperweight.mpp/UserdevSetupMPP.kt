package dev.racci.paperweight.mpp

import io.papermc.paperweight.userdev.internal.setup.UserdevSetup
import io.papermc.paperweight.userdev.internal.setup.extractDevBundle
import io.papermc.paperweight.util.MavenDep
import io.papermc.paperweight.util.constants.IVY_REPOSITORY
import io.papermc.paperweight.util.constants.paperSetupOutput
import io.papermc.paperweight.util.path
import io.papermc.paperweight.util.setupIvyRepository
import org.gradle.api.Project
import org.gradle.api.artifacts.DependencySet
import org.gradle.api.artifacts.repositories.IvyArtifactRepository
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.services.BuildService
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.repositories
import java.nio.file.Path

abstract class UserdevSetupMPP : BuildService<UserdevSetup.Parameters>, SetupHandlerMPP {
    internal lateinit var cachedProject: Project

    companion object {
        val LOGGER: Logger = Logging.getLogger(UserdevSetupMPP::class.java)
    }

    private val extractDevBundle = extractDevBundle(
        parameters.cache.path.resolve(paperSetupOutput("extractDevBundle", "dir")),
        parameters.bundleZip.path
    )

    internal fun setup(project: Project) {
        cachedProject = project
    }

    private val setup: SetupHandlerMPP by lazy { createSetup() }

    private fun createSetup(): SetupHandlerMPP = SetupHandlerMPP.create(this, extractDevBundle)

    fun addIvyRepository() {
        cachedProject.repositories {
            setupIvyRepository(parameters.cache.path.resolve(IVY_REPOSITORY)) {
                configureIvyRepo(this)
            }
        }
    }

    // begin delegate to setup
    override fun createOrUpdateIvyRepository(context: SetupHandlerMPP.Context) {
        setup.createOrUpdateIvyRepository(context)
    }

    override fun configureIvyRepo(repo: IvyArtifactRepository) {
        setup.configureIvyRepo(repo)
    }

    override fun populateCompileConfiguration(context: SetupHandlerMPP.Context, dependencySet: DependencySet) {
        setup.populateCompileConfiguration(context, dependencySet)
    }

    override fun populateRuntimeConfiguration(context: SetupHandlerMPP.Context, dependencySet: DependencySet) {
        setup.populateRuntimeConfiguration(context, dependencySet)
    }

    override fun serverJar(context: SetupHandlerMPP.Context): Path {
        return setup.serverJar(context)
    }

    override val serverJar: Path
        get() = setup.serverJar

    override val reobfMappings: Path
        get() = setup.reobfMappings

    override val minecraftVersion: String
        get() = setup.minecraftVersion

    override val pluginRemapArgs: List<String>
        get() = setup.pluginRemapArgs

    override val paramMappings: MavenDep
        get() = setup.paramMappings

    override val decompiler: MavenDep
        get() = setup.decompiler

    override val remapper: MavenDep
        get() = setup.remapper

    override val libraryRepositories: List<String>
        get() = setup.libraryRepositories
    // end delegate to setup
}
