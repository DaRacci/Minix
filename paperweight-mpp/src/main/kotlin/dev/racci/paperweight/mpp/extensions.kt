package dev.racci.paperweight.mpp // ktlint-disable filename

import io.papermc.paperweight.tasks.RemapJar
import io.papermc.paperweight.userdev.internal.setup.util.genSources
import io.papermc.paperweight.util.cache
import io.papermc.paperweight.util.constants.DEV_BUNDLE_CONFIG
import io.papermc.paperweight.util.convertToPath
import io.papermc.paperweight.util.download
import io.papermc.paperweight.util.set
import io.papermc.paperweight.util.sha256asHex
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.tasks.TaskProvider
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.registerIfAbsent
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.DefaultKotlinDependencyHandler
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(PaperweightMppPlugin::class.java)

internal val KotlinSourceSet.groupName: String get() = name.takeWhile { !it.isUpperCase() }

internal fun lowerCamelCaseName(vararg names: String): String {
    val nonEmptyParts = names.mapNotNull { it.takeIf(String::isNotEmpty) }
    return nonEmptyParts.drop(1).joinToString(
        separator = "",
        prefix = nonEmptyParts.firstOrNull().orEmpty(),
        transform = String::capitalized
    )
}

internal fun KotlinSourceSet.disambiguateName(simpleName: String): String {
    val nameParts = listOfNotNull(this.name.takeIf { it != "main" }, simpleName)
    return lowerCamelCaseName(*nameParts.toTypedArray())
}

internal fun KotlinSourceSet.disambiguateGroupName(simpleName: String): String {
    val nameParts = listOfNotNull(this.groupName.takeIf { it != "main" }, simpleName)
    return lowerCamelCaseName(*nameParts.toTypedArray())
}

internal fun KotlinTarget.disambiguateName(simpleName: String): String = lowerCamelCaseName(name, simpleName)

internal fun KotlinSourceSet.devBundleConfiguration(project: Project): Configuration = with(project) {
    configurations.create(disambiguateName(DEV_BUNDLE_CONFIG)) {
        isTransitive = true
        configurations[compileOnlyConfigurationName].extendsFrom(this)
    }
}

internal fun Project.kotlinMPP() = project.extensions.getByName("kotlin") as KotlinMultiplatformExtension

public val KotlinTarget.reobfJar: TaskProvider<RemapJar>
    get() = project.tasks.named<RemapJar>(disambiguateName("reobfJar")) {}

public fun KotlinTarget.reobfJar(
    configure: RemapJar.() -> Unit
): TaskProvider<RemapJar> = project.tasks.named<RemapJar>(disambiguateName(name)) {
    configure()
}

public fun KotlinDependencyHandler.paperweightDevBundle(
    group: String,
    version: String? = null,
    artifactId: String = "dev-bundle",
    configurationAction: ExternalModuleDependency.() -> Unit = {}
) {
    val sourceSet = (this as DefaultKotlinDependencyHandler).parent as KotlinSourceSet
    val configuration = sourceSet.devBundleConfiguration(project)

    logger.info("Adding dev bundle for ${sourceSet.name} with configuration name ${configuration.name}")
    addDependencyByStringNotation(configuration.name, "$group:$artifactId:$version", configurationAction)

    val devBundleZip = project.configurations.named(sourceSet.disambiguateName(DEV_BUNDLE_CONFIG))
        .map { it.singleFile }.convertToPath()
    val serviceName = "paperweight-USERDEV:setupService:${devBundleZip.sha256asHex()}"

    PaperweightMppPlugin.USERDEV[sourceSet] = project.gradle.sharedServices.registerIfAbsent(
        serviceName,
        UserdevSetupMPP::class
    ) {
        parameters {
            cache.set(project.layout.cache.resolve(sourceSet.name).toFile())
            bundleZip.set(devBundleZip)
            downloadService.set(project.download)
            genSources.set(project.genSources) // Might need change
        }
    }.get().apply {
        setup(project)
        PaperweightMppPlugin.configureKotlinCompilation(sourceSet, project, this)
    }
}

public fun KotlinDependencyHandler.addDependencyByStringNotation(
    configurationName: String,
    dependencyNotation: Any,
    configure: ExternalModuleDependency.() -> Unit = { }
): ExternalModuleDependency = addDependency(
    configurationName,
    project.dependencies.create(dependencyNotation) as ExternalModuleDependency,
    configure
)

public fun <T : Dependency> KotlinDependencyHandler.addDependency(
    configurationName: String,
    dependency: T,
    configure: T.() -> Unit
): T =
    dependency.also {
        configure(it)
        project.dependencies.add(configurationName, it)
    }
