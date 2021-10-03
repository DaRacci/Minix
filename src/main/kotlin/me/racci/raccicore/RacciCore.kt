package me.racci.raccicore

import co.aikar.commands.BaseCommand
import me.racci.raccicore.data.PlayerManager
import me.racci.raccicore.listeners.*
import me.racci.raccicore.runnables.TimeRunnable
import org.bukkit.scheduler.BukkitTask

/**
 * Racci core
 */
internal lateinit var racciCore : RacciCore ; private set

/**
 * Player manager
 */
internal lateinit var playerManager : PlayerManager ; private set

/**
 * Racci lib
 *
 * @constructor Create empty Racci lib
 */
class RacciCore : RacciPlugin(
    "&8",
    "RacciCore"
) {

    override fun onEnable() {
        racciCore = this
        playerManager = PlayerManager()
    }

    override fun onDisable() {
        playerManager.playerDataMap.clear()
    }

    override fun registerListeners(): List<KotlinListener> {
        return listOf(
            PlayerMoveListener(),
            PlayerTeleportListener(),
            PlayerMoveFullXYZListener(),
            PlayerComboListener(),
            PlayerJoinLeaveListener(),
            TimeRunnable(),
        )
    }

    override fun registerRunnables(): List<BukkitTask> {
        return listOf(
            TimeRunnable().runTaskTimerAsynchronously(this, 0L, 20)
        )

    }
}