package dev.racci.minix.api.plugin

import co.aikar.commands.BaseCommand
import dev.racci.minix.api.extensions.KotlinListener
import org.bukkit.plugin.java.JavaPlugin

sealed class SusPlugin : JavaPlugin() {

    /**
     * This is called when the server picks up your Plugin and has begun loading it.
     */
    open suspend fun handleLoad() {}

    /**
     * This is called Once the Plugin is ready to accept and register
     * events, commands etc.
     */
    open suspend fun handleEnable() {}

    /**
     * This will be called after your Plugin is done loading
     * and Minix has finished its loading process for your Plugin.
     */
    open suspend fun handleAfterLoad() {}

    /**
     * This will be called whenever [MinixPlugin.reload] is called.
     */
    open suspend fun handleReload() {}

    /**
     * This is triggered when your Plugin is being disabled by the Server,
     * Please use this to clean up your Plugin to not leak resources.
     */
    open suspend fun handleDisable() {}

    /**
     * The returned list of [KotlinListener]'s will be registered,
     * and enabled during the enable process.
     */
    open suspend fun registerListeners(): List<KotlinListener> = emptyList()

    /**
     * The returned list of [BaseCommand]'s will be registered,
     * and enabled during the enable process.
     */
    open suspend fun registerCommands(): List<BaseCommand> = emptyList()
}
