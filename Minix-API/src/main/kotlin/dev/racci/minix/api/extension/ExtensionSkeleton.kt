package dev.racci.minix.api.extension

import dev.racci.minix.api.annotations.MinixInternal
import dev.racci.minix.api.extensions.KListener
import dev.racci.minix.api.extensions.WithPlugin
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.Closeable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import org.koin.core.qualifier.Qualifier

interface ExtensionSkeleton<P : MinixPlugin> : WithPlugin<P>, Qualifier {

    @MinixInternal
    val dispatcher: Closeable<ExecutorCoroutineDispatcher>

    /** The Listener, which is used to register events in the extension. */
    @MinixInternal
    val eventListener: KListener<P>

    /** If the extension is loaded. */
    val loaded: Boolean

    /** The name of the extension. */
    val name: String

    /** The current state of the extension. */
    val state: ExtensionState

    /**
     * The supervisor scope, which is used to launch coroutines in the
     * extension.
     */
    @MinixInternal
    val supervisor: CoroutineScope

    /**
     * Handles the enabling of the extension.
     * This is called once the plugin has finished loading and is considered enabled.
     *
     * ## Enabling can occur multiple times during the lifetime of the extension.
     */
    suspend fun handleEnable() = Unit

    /**
     * Handles the loading of the extension.
     * This is called while the plugin is being loaded and is not yet considered enabled.
     *
     * ## Loading only occurs once when this extension is found from reflection.
     */
    suspend fun handleLoad() = Unit

    /**
     * Handles the unloading of the extension.
     * This is called while the plugin is disabling and is considered disabled.
     *
     * ## Unloading only occurs once when server is shutting down.
     */
    suspend fun handleUnload() = Unit

    /**
     * Handles the disabling of the extension.
     * This is called when the plugin is disabled and is considered disabled.
     *
     * ## Unloading can occur multiple times during the lifetime of the extension.
     */
    suspend fun handleDisable() = Unit
}
