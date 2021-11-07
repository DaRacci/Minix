package me.racci.raccicore

import com.github.shynixn.mccoroutine.asyncDispatcher
import com.github.shynixn.mccoroutine.launch
import com.github.shynixn.mccoroutine.launchAsync
import com.github.shynixn.mccoroutine.minecraftDispatcher
import kotlinx.coroutines.CoroutineScope
import me.racci.raccicore.data.PlayerManager
import me.racci.raccicore.listeners.PlayerComboListener
import me.racci.raccicore.listeners.PlayerMoveFullXYZListener
import me.racci.raccicore.listeners.PlayerMoveListener
import me.racci.raccicore.listeners.PlayerTeleportListener
import me.racci.raccicore.runnables.TimeRunnable
import me.racci.raccicore.utils.extensions.KotlinListener
import org.bukkit.NamespacedKey
import kotlin.properties.Delegates

class RacciCore : RacciPlugin(
    "&2",
    "RacciCore"
) {

    companion object {

        internal var instance               by Delegates.notNull<RacciCore>()
        internal val asyncDispatcher        get() = instance.asyncDispatcher
        internal val minecraftDispatcher    get() = instance.minecraftDispatcher
        internal val log                    get() = instance.log

        internal fun namespacedKey(value: String) = NamespacedKey(instance, value)
        internal fun launch(f: suspend CoroutineScope.() -> Unit) = instance.launch(f)
        internal fun launchAsync(f: suspend CoroutineScope.() -> Unit) = instance.launchAsync(f)

    }

    override suspend fun handleEnable() {
        instance = this
        PlayerManager.init()
    }

    override suspend fun handleAfterLoad() {
        TimeRunnable()
            .runAsyncTaskTimer(this, 5L, 20L)
    }

    override suspend fun handleDisable() {
        PluginManager.close()
        PlayerManager.close()
    }

    override suspend fun registerListeners(): List<KotlinListener> {
        return listOf(
            PlayerMoveListener(),
            PlayerTeleportListener(),
            PlayerMoveFullXYZListener(),
            PlayerComboListener(),
            TimeRunnable(),
        )
    }
}