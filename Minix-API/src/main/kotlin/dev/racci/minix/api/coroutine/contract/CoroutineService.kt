package dev.racci.minix.api.coroutine.contract

import dev.racci.minix.api.plugin.MinixPlugin
import org.koin.core.component.KoinComponent

interface CoroutineService : KoinComponent {

    /**
     * Get a plugins active coroutine session.
     *
     * @return The active coroutine session.
     */
    fun getCoroutineSession(plugin: MinixPlugin): CoroutineSession

    /**
     * Disables coroutine for the given plugin.
     */
    fun disable(plugin: MinixPlugin)
}
