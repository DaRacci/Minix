package me.racci.raccicore.core

import co.aikar.commands.PaperCommandManager
import com.github.shynixn.mccoroutine.launchAsync
import com.github.shynixn.mccoroutine.registerSuspendingEvents
import me.racci.raccicore.api.extensions.KListener
import me.racci.raccicore.api.extensions.event
import me.racci.raccicore.api.extensions.pm
import me.racci.raccicore.api.lifecycle.Lifecycle
import me.racci.raccicore.api.lifecycle.LifecycleEvent
import me.racci.raccicore.api.lifecycle.LifecycleListener
import me.racci.raccicore.api.plugin.RacciPlugin
import me.racci.raccicore.api.utils.UpdateChecker
import me.racci.raccicore.core.scheduler.CoroutineScheduler
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.event.server.PluginEnableEvent
import kotlin.reflect.KClass

class PluginManager(
    override val plugin: RacciCore
): LifecycleListener<RacciCore>, KListener<RacciCore> {

    private val loadedPlugins = HashMap<KClass<out RacciPlugin>, RacciPlugin>()

    override suspend fun onEnable() {
        event<PluginEnableEvent> {
            if(plugin is RacciPlugin) {
                plugin.launchAsync {
                    val p = plugin as RacciPlugin
                    p.log.info("")
                    p.log.info("Loading ${p.name}")
                    p.log.info("")

                    loadedPlugins[p::class] = p
                    p.commandManager = PaperCommandManager(p)

                    if(p.spigotId != 0) UpdateChecker(p)
                    //if(p.bStatsId != 0) {}//TODO

                    // Allow the plugin to do its thing before we register events etc.
                    p.handleEnable()

                    p.registerLifecycles().map {Lifecycle(it.second, it.first)}.forEach(p.lifecycleListeners::add)
                    p.lifecycleLoadOrder.forEach {it.listener(LifecycleEvent.ENABLE)}
                    p.registerListeners().forEach {pm.registerSuspendingEvents(it, p)}
                    p.registerCommands().forEach(p.commandManager::registerCommand)

                    p.log.info("")
                    p.log.success("Finished Loading ${p.name}")
                    p.log.info("")

                    p.log.debug("HandleAfterLoad Started")
                    p.handleAfterLoad()
                }
            }
        }
        event<PluginDisableEvent> {
            if(plugin is RacciPlugin) {
                plugin.launchAsync {
                    val p = plugin as RacciPlugin
                    p.handleDisable()
                    p.lifecycleDisableOrder.forEach {it.listener(LifecycleEvent.DISABLE)}
                    p.commandManager.unregisterCommands()
                    loadedPlugins.remove(p::class)
                    CoroutineScheduler.cancelAllTasks(p)
                }
            }
        }
    }

    override suspend fun onDisable() {
        loadedPlugins.values.forEach(pm::disablePlugin)
        loadedPlugins.clear()
    }

}