package dev.racci.minix.api.annotations

import com.github.benmanes.caffeine.cache.Caffeine
import dev.racci.minix.api.extension.ExtensionSkeleton
import org.jetbrains.annotations.ApiStatus.AvailableSince
import org.jetbrains.annotations.ApiStatus.Experimental
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

private typealias ExtensionClass = KClass<out ExtensionSkeleton<*>>

/**
 * Defines the dependencies for this extension,
 * Each dependency will be loaded before this extension is able to load.
 * If one of the dependencies fails to load, this extension will not load either.
 *
 * @property dependencies The other extensions that this requires to be loaded, Note this must be an [dev.racci.minix.api.extension.Extension] however it needs to be compiled as any KClass.
 */
// TODO: Add support for depends between plugins, and allow specifying by QualifierValue.
@Experimental
@AvailableSince("5.0.0")
@MinixReflectiveAPI
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
public annotation class Depends(val dependencies: Array<KClass<*>>) {
    public companion object {
        private val dependencies = Caffeine.newBuilder()
            .expireAfterAccess(10, TimeUnit.SECONDS)
            .build<ExtensionClass, Set<ExtensionClass>> { it::class.findAnnotation<Depends>()?.dependencies?.filterIsInstance<ExtensionClass>()?.toSet().orEmpty() }

        public fun dependenciesOf(
            extension: ExtensionClass
        ): Sequence<ExtensionClass> = sequence {
            val deps = dependencies.get(extension)

            if (deps.isNotEmpty()) {
                yieldAll(deps)
                yieldAll(deps.flatMap(::dependenciesOf))
            }
        }

        public fun dependsOn(
            extension: ExtensionClass,
            possibleDepend: ExtensionClass
        ): Boolean = catchingOverflow(extension).contains(possibleDepend)

        private fun catchingOverflow(extension: ExtensionClass): Sequence<ExtensionClass> {
            return try {
                dependenciesOf(extension)
            } catch (e: StackOverflowError) {
                error("Cyclic dependency detected in `$extension`.")
            }
        }
    }
}
