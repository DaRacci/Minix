package me.racci.raccicore

import me.racci.raccicore.data.PlayerManager
import me.racci.raccicore.listeners.PlayerComboListener
import me.racci.raccicore.listeners.PlayerMoveFullXYZListener
import me.racci.raccicore.listeners.PlayerMoveListener
import me.racci.raccicore.listeners.PlayerTeleportListener
import me.racci.raccicore.runnables.KotlinRunnable
import me.racci.raccicore.runnables.TimeRunnable
import me.racci.raccicore.utils.extensions.KotlinListener

class RacciCore : RacciPlugin(
    "&8",
    "RacciCore"
) {

    internal companion object {
        lateinit var instance        : RacciCore
    }

    override suspend fun handleEnable() {
        instance = this
        PlayerManager.init(this)
    }

    override suspend fun handleDisable() {
        RacciPluginHandler.close()
        PlayerManager.close()
    }

    override suspend fun registerListeners(): List<KotlinListener> {
        return listOf(
            PlayerMoveListener(),
            PlayerTeleportListener(),
            PlayerMoveFullXYZListener(),
            PlayerComboListener(),
            TimeRunnable(this),
        )
    }

    override suspend fun registerRunnables(): List<KotlinRunnable> {
        return listOf(
            TimeRunnable(this)
        )
    }
}