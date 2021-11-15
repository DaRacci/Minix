package me.racci.raccicore.core

import com.github.shynixn.mccoroutine.asyncDispatcher
import com.github.shynixn.mccoroutine.launch
import com.github.shynixn.mccoroutine.launchAsync
import com.github.shynixn.mccoroutine.minecraftDispatcher
import kotlinx.coroutines.CoroutineScope
import me.racci.raccicore.RacciPlugin
import me.racci.raccicore.core.data.PlayerManager
import me.racci.raccicore.core.listeners.PlayerComboListener
import me.racci.raccicore.core.listeners.PlayerMoveFullXYZListener
import me.racci.raccicore.core.listeners.PlayerMoveListener
import me.racci.raccicore.core.listeners.PlayerTeleportListener
import me.racci.raccicore.core.runnables.TimeRunnable
import me.racci.raccicore.extensions.KotlinListener
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.NamespacedKey
import kotlin.properties.Delegates

class RacciCore : RacciPlugin(
    NamedTextColor.AQUA,
    Component.text("RacciCore").color(NamedTextColor.GREEN)
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