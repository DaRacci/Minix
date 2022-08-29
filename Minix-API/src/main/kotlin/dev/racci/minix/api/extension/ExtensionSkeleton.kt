package dev.racci.minix.api.extension

import dev.racci.minix.api.annotations.MinixInternal
import dev.racci.minix.api.extensions.KListener
import dev.racci.minix.api.extensions.WithPlugin
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.logger.MinixLogger
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval
import org.koin.core.qualifier.Qualifier
import kotlin.reflect.KClass

interface ExtensionSkeleton<P : MinixPlugin> : WithPlugin<P>, Qualifier {
    /** The Listener, which is used to register events in the extension. */
    @MinixInternal
    val eventListener: KListener<P>

    /** The supervisor scope, which is used to launch coroutines in the extension. */
    @MinixInternal
    val supervisor: CoroutineScope

    /** The required Extensions for this Extension to load. */
    @MinixInternal
    val dependencies: ImmutableSet<KClass<out Extension<*>>>

    /** If the extension has been bound in koin. */
    @MinixInternal
    var bound: Boolean

    /** An external Class to bind to in koin. */
    @MinixInternal
    val bindToKClass: KClass<*>?

    /** The name of the extension. */
    val name: String

    /** The MinixLogger instance from the plugin. */
    @get:ScheduledForRemoval(inVersion = "4.0.0")
    @Deprecated("Use the WithPlugin extension instead.", ReplaceWith("WithPlugin<*>.log", "dev.racci.minix.api.extensions.ExPlugin"))
    val log: MinixLogger

    /** The current state of the extension. */
    val state: ExtensionState

    /** If the extension is loaded. */
    val loaded: Boolean

    /** Called when the plugin loading and not yet enabled. */
    suspend fun handleLoad()

    /** Called when the plugin has finished loading and is enabled. */
    suspend fun handleEnable()

    /** Called when the plugin is being disabled. */
    suspend fun handleUnload()
}
