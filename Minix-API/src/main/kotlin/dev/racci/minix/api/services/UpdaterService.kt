package dev.racci.minix.api.services

import dev.racci.minix.api.data.PluginUpdater
import dev.racci.minix.api.updater.UpdateResult
import dev.racci.minix.api.utils.getKoin

interface UpdaterService {
    val enabledUpdaters: MutableList<PluginUpdater>
    val disabledUpdaters: MutableList<PluginUpdater>

    /**
     * Run an update check for all enabled updaters.
     *
     * @return An array of pairs of updater name and their update results.
     */
    suspend fun updateAll(): Array<Pair<String, UpdateResult>>

    /**
     * Check all the updaters for updates.
     *
     * @return A array of pairs of the updater name and the true if there is an update.
     */
    fun checkAll(): Array<Pair<String, Boolean>>

    /**
     * Tries to update this updater.
     * This will not set the last time for the scheduled task.
     *
     * @param updater The updater to update.
     * @return The update result.
     */
    suspend fun tryUpdate(updater: PluginUpdater): UpdateResult

    /**
     * Tries to check if this updater has an update.
     *
     * @param updater The updater to check.
     * @return The true if there is an update.
     */
    fun checkForUpdate(updater: PluginUpdater): Boolean

    companion object : UpdaterService by getKoin().get()
}
