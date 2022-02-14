package dev.racci.minix.api.coroutine.contract

import dev.racci.minix.api.plugin.MinixPlugin

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
