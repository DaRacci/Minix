package dev.racci.minix.api.services

import dev.racci.minix.api.data.PluginUpdater
import dev.racci.minix.api.updater.UpdateResult
import dev.racci.minix.api.utils.getKoin
import org.apiguardian.api.API

@API(status = API.Status.EXPERIMENTAL, since = "2.4.0")
interface UpdaterService {
    val enabledUpdaters: MutableList<PluginUpdater>
    val disabledUpdaters: MutableList<PluginUpdater>

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
