package dev.racci.paperweight.mpp

import io.papermc.paperweight.userdev.ReobfArtifactConfiguration
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

/**
 * Extension exposing configuration and other APIs for paperweight USERDEV.
 */
public abstract class PaperweightUserExtensionMPP(
    sourceSet: KotlinSourceSet,
    objects: ObjectFactory
) {
    /**
     * Whether to inject the Paper maven repository for use by the dev bundle configuration.
     *
     * True by default to allow easily resolving Paper development bundles.
     */
    public val injectPaperRepository: Property<Boolean> = objects.property<Boolean>().convention(true)

    /**
     * The [ReobfArtifactConfiguration] is responsible for setting the input and output jars for `reobfJar`,
     * as well as changing the classifiers of other jars (i.e. `jar` or `shadowJar`).
     */
    public val reobfArtifactConfiguration: Property<ReobfArtifactConfigurationMPP> = objects
        .property<ReobfArtifactConfigurationMPP>().convention(ReobfArtifactConfigurationMPP.REOBF_PRODUCTION)

    /**
     * Provides the Minecraft version of the current dev bundle.
     */
    public val minecraftVersion: String? by lazy { PaperweightMppPlugin.USERDEV[sourceSet]?.minecraftVersion }
}
