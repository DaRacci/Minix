package dev.racci.minix.api.plugin

import org.apiguardian.api.API
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus
import org.koin.core.component.get
import org.koin.core.qualifier.Qualifier

/**
 * The main plugin class.
 */
public actual abstract class MinixPlugin : Qualifier, WithPlugin<MinixPlugin>, JavaPlugin() {
    actual final override val value: String = this.name

    protected actual open suspend fun handleEnabled() {
        /* no-op */
    }

    protected actual open suspend fun handleDisable() {
        /* no-op */
    }

    protected actual open suspend fun handleLoad() {
        /* no-op */
    }

    protected actual open suspend fun handleReload() {
        /* no-op */
    }

    protected actual open suspend fun handleUnload() {
        /* no-op */
    }

    /* This is called while the plugin is considered disabled. */
    protected actual open suspend fun handleAfterLoad() {
        /* no-op */
    }

    protected actual open suspend fun handleAfterEnable() {
        /* no-op */
    }

    @ApiStatus.NonExtendable
    @API(status = API.Status.INTERNAL)
    override fun onEnable() {
        get<PluginService>().startPlugin(this)
    }

    @ApiStatus.NonExtendable
    @API(status = API.Status.INTERNAL)
    override fun onDisable() {
        get<PluginService>().unloadPlugin(this)
    }

    @ApiStatus.NonExtendable
    @API(status = API.Status.INTERNAL)
    override fun onLoad() {
        get<PluginService>().loadPlugin(this)
    }
}
