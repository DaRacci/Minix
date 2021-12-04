@file:Suppress("UNUSED")
package me.racci.raccicore.core

import co.aikar.commands.BaseCommand
import com.github.shynixn.mccoroutine.asyncDispatcher
import com.github.shynixn.mccoroutine.launch
import com.github.shynixn.mccoroutine.launchAsync
import com.github.shynixn.mccoroutine.minecraftDispatcher
import kotlinx.coroutines.CoroutineScope
import me.racci.raccicore.api.extensions.KotlinListener
import me.racci.raccicore.api.lifecycle.LifecycleEvent
import me.racci.raccicore.api.lifecycle.LifecycleListener
import me.racci.raccicore.api.plugin.RacciPlugin
import me.racci.raccicore.core.commands.CoreCommand
import me.racci.raccicore.core.listeners.*
import me.racci.raccicore.core.managers.BungeeCordManager
import me.racci.raccicore.core.managers.CommandManager
import me.racci.raccicore.core.managers.HookManager
import me.racci.raccicore.core.managers.PlayerManager
import me.racci.raccicore.core.runnables.TimeRunnable
import org.bukkit.NamespacedKey
import kotlin.properties.Delegates

class RacciCore : RacciPlugin(
    "&2RacciCore"
) {

    internal companion object {

        val log             get() = instance.log
        var instance by Delegates.notNull<RacciCore>()
        val asyncDispatcher get() = instance.asyncDispatcher
        val syncDispatcher get() = instance.minecraftDispatcher

        fun namespacedKey(value: String) = NamespacedKey(instance, value)
        fun launch(f: suspend CoroutineScope.() -> Unit) = instance.launch(f)
        fun launchAsync(f: suspend CoroutineScope.() -> Unit) = instance.launchAsync(f)

    }

    override suspend fun onLoadAsync() {
    }

    override suspend fun handleEnable() {
        instance = this
        PluginManager(this).invoke(LifecycleEvent.ENABLE)
    }

    override suspend fun handleAfterLoad() {
        TimeRunnable()
                .runTaskTimer(this, 5L, 20L)
    }

    override suspend fun registerCommands(): List<BaseCommand> {
        return listOf(
            CoreCommand()
        )
    }

    override suspend fun registerListeners(): List<KotlinListener> {
        return listOf(
            PlayerMoveListener(),
            PlayerMoveXYZListener(),
            PlayerTeleportListener(),
            PlayerMoveFullXYZListener(),
            PlayerComboListener(),
            TimeRunnable(),
        )
    }

    override suspend fun registerLifecycles(): List<Pair<LifecycleListener<*>, Int>> {
        return listOf(
            PluginManager(this) to 1,
            HookManager(this) to 2,
            BungeeCordManager(this) to 3,
            CommandManager(this) to 4,
            PlayerManager(this) to 5,
        )
    }
}