package dev.racci.paperweight.mpp

import io.papermc.paperweight.userdev.ReobfArtifactConfiguration
import io.papermc.paperweight.util.*
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.kotlin.dsl.*
import org.gradle.workers.WorkerExecutor
import java.util.Optional

/**
 * Extension exposing configuration and other APIs for paperweight userdev.
 */
abstract class PaperweightUserExtensionMPP(
    project: Project,
    workerExecutor: WorkerExecutor,
    javaToolchainService: JavaToolchainService,
    setup: Provider<Optional<SetupHandlerMPP>>,
    objects: ObjectFactory
) {
    /**
     * Whether to inject the Paper maven repository for use by the dev bundle configuration.
     *
     * True by default to allow easily resolving Paper development bundles.
     */
    val injectPaperRepository: Property<Boolean> = objects.property<Boolean>().convention(true)

    /**
     * The [ReobfArtifactConfiguration] is responsible for setting the input and output jars for `reobfJar`,
     * as well as changing the classifiers of other jars (i.e. `jar` or `shadowJar`).
     */
    val reobfArtifactConfiguration: Property<ReobfArtifactConfiguration> = objects.property<ReobfArtifactConfiguration>()
        .convention(ReobfArtifactConfiguration.REOBF_PRODUCTION)

    /**
     * Provides the Minecraft version of the current dev bundle.
     */
    val minecraftVersion: Provider<String> = objects.property<String>().value(
        setup.map { it.get().minecraftVersion }
    ).withDisallowChanges().withDisallowUnsafeRead()
}
