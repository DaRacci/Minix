package dev.racci.paperweight.mpp // ktlint-disable filename

import io.papermc.paperweight.util.constants.DEV_BUNDLE_CONFIG
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.configurationcache.extensions.capitalized
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.DefaultKotlinDependencyHandler

private fun prefixedPair(prefix: String, str: String): String = buildString {
    append(prefix)
    append(str.capitalized())
}

internal fun KotlinTarget.prefixedName(str: String): String = prefixedPair(name, str)

internal fun KotlinSourceSet.prefixedName(str: String): String = prefixedPair(name, str)

internal fun Project.kotlinMPP() = project.extensions.getByName("kotlin") as KotlinMultiplatformExtension

fun KotlinDependencyHandler.paperweightDevBundle(
    group: String,
    version: String? = null,
    artifactId: String = "dev-bundle",
    configurationAction: ExternalModuleDependency.() -> Unit = {}
) {
    val targetName = (this as DefaultKotlinDependencyHandler).parent.apiConfigurationName.substringBeforeLast("Api")
    val devBundleConfigurationName = prefixedPair(targetName, DEV_BUNDLE_CONFIG)
    addDependencyByStringNotation(devBundleConfigurationName, "$group:$artifactId:$version", configurationAction)
}

fun KotlinDependencyHandler.addDependencyByStringNotation(
    configurationName: String,
    dependencyNotation: Any,
    configure: ExternalModuleDependency.() -> Unit = { }
): ExternalModuleDependency = addDependency(configurationName, project.dependencies.create(dependencyNotation) as ExternalModuleDependency, configure)

fun <T : Dependency> KotlinDependencyHandler.addDependency(
    configurationName: String,
    dependency: T,
    configure: T.() -> Unit
): T =
    dependency.also {
        configure(it)
        project.dependencies.add(configurationName, it)
    }
