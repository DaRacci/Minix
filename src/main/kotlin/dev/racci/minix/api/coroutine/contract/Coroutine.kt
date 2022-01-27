package dev.racci.minix.api.coroutine.contract

import dev.racci.minix.api.plugin.MinixPlugin
import org.jetbrains.annotations.ApiStatus

@ApiStatus.AvailableSince("1.0.0")
interface Coroutine {

    /**
     * Get coroutine session for the given plugin.
     */
    fun getCoroutineSession(plugin: MinixPlugin): CoroutineSession

    /**
     * Disables coroutine for the given plugin.
     */
    fun disable(plugin: MinixPlugin)
}
