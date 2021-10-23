package me.racci.raccicore

import me.racci.raccicore.data.PlayerManager
import me.racci.raccicore.listeners.*
import me.racci.raccicore.runnables.TimeRunnable
import me.racci.raccicore.utils.extensions.KotlinListener
import org.bukkit.scheduler.BukkitTask

/**
 * Racci core
 */
internal val plugin : RacciCore
    get() = RacciCore.plugin
/**
 * Racci lib
 *
 * @constructor Create empty Racci lib
 */
class RacciCore : RacciPlugin(
    "&8",
    "RacciCore"
) {

    companion object {
        lateinit var plugin : RacciCore
    }

    override suspend fun onEnableAsync() {
        plugin = this
        super.onEnableAsync()
    }

    override suspend fun handleAfterLoad() {
        PlayerManager.init()
    }

    override suspend fun handleDisable() {
        PlayerManager.shutdown()
    }

    override suspend fun registerListeners(): List<KotlinListener> {
        return listOf(
            PlayerMoveListener(),
            PlayerTeleportListener(),
            PlayerMoveFullXYZListener(),
            PlayerComboListener(),
            PlayerJoinLeaveListener(),
            TimeRunnable(),
        )
    }

    override suspend fun registerRunnables(): List<BukkitTask> {
        return listOf(
            TimeRunnable().runTaskTimerAsynchronously(this, 0L, 20)
        )
    }
}