package me.racci.raccicore

import com.github.shynixn.mccoroutine.launch
import com.github.shynixn.mccoroutine.launchAsync
import kotlinx.coroutines.CoroutineScope
import me.racci.raccicore.data.PlayerManager
import me.racci.raccicore.listeners.PlayerComboListener
import me.racci.raccicore.listeners.PlayerMoveFullXYZListener
import me.racci.raccicore.listeners.PlayerMoveListener
import me.racci.raccicore.listeners.PlayerTeleportListener
import me.racci.raccicore.runnables.KotlinRunnable
import me.racci.raccicore.runnables.TimeRunnable
import me.racci.raccicore.utils.extensions.KotlinListener

class RacciCore : RacciPlugin(
    "&2",
    "RacciCore"
) {

    internal companion object {
        lateinit var instance : RacciCore
        fun launch(f: suspend CoroutineScope.() -> Unit) = instance.launch(f)
        fun launchAsync(f: suspend CoroutineScope.() -> Unit) = instance.launchAsync(f)
        val log get() = instance.log
    }

    override suspend fun handleEnable() {
        instance = this
        log.debugMode
        log.debug("Test")
        instance.log.debug("test")
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