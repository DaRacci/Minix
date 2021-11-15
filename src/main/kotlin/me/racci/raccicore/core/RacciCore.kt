package me.racci.raccicore.core

import com.github.shynixn.mccoroutine.asyncDispatcher
import com.github.shynixn.mccoroutine.launch
import com.github.shynixn.mccoroutine.launchAsync
import com.github.shynixn.mccoroutine.minecraftDispatcher
import kotlinx.coroutines.CoroutineScope
import me.racci.raccicore.RacciPlugin
import me.racci.raccicore.core.listeners.PlayerComboListener
import me.racci.raccicore.core.listeners.PlayerMoveFullXYZListener
import me.racci.raccicore.core.listeners.PlayerMoveListener
import me.racci.raccicore.core.listeners.PlayerTeleportListener
import me.racci.raccicore.core.managers.BungeeCordManager
import me.racci.raccicore.core.managers.CommandManager
import me.racci.raccicore.core.managers.PlayerManager
import me.racci.raccicore.core.runnables.TimeRunnable
import me.racci.raccicore.extensions.KotlinListener
import me.racci.raccicore.lifecycle.LifecycleListener
import org.bukkit.NamespacedKey
import kotlin.properties.Delegates

class RacciCore : RacciPlugin(
    "&2RacciCore"
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
    }

    override suspend fun handleAfterLoad() {
        TimeRunnable()
            .runAsyncTaskTimer(this, 5L, 20L)
    }

    override suspend fun handleDisable() {
        PluginManager.close()
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

    override suspend fun registerLifecycles(): List<Pair<LifecycleListener<*>, Int>> {
        return listOf(
            BungeeCordManager(this) to 1,
            CommandManager(this) to 1,
            PlayerManager(this) to 1,
        )
    }
}