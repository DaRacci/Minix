package dev.racci.minix.api.plugin

import dev.racci.minix.api.annotations.MinixDsl
import dev.racci.minix.api.extension.ExtensionStateEvent
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.core.services.ExtensionUnit
import dev.racci.minix.core.services.ListenerUnit
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus

/**
 * The superclass replacing [JavaPlugin],
 * This class provides many more features and allows implementation into the
 * systems of Minix.
 */
@Suppress("LeakingThis")
open class MinixPlugin : JavaPlugin(), SusPlugin {

    val log: MinixLogger get() = getKoin().get<PluginService>()[this].log

    @ApiStatus.Internal
    @ApiStatus.NonExtendable
    override fun onEnable() {
        getKoin().get<PluginService>().startPlugin(this)
    }

    @ApiStatus.Internal
    @ApiStatus.NonExtendable
    override fun onDisable() {
        getKoin().get<PluginService>().unloadPlugin(this)
    }

    @ApiStatus.Internal
    @ApiStatus.NonExtendable
    override fun onLoad() {
        getKoin().get<PluginService>().loadPlugin(this)
    }

    @ApiStatus.Internal
    @ApiStatus.NonExtendable
    suspend inline fun send(event: ExtensionStateEvent) {
        getKoin().get<PluginService>()[this].extensionEvents.emit(event)
    }

    @MinixDsl
    protected suspend fun extensions(builder: suspend ExtensionsBuilder.() -> Unit) {
        builder(getKoin().get<PluginService>()[this].extensionsBuilder)
    }

    @MinixDsl
    protected suspend fun listeners(builder: suspend ListenerBuilder.() -> Unit) {
        builder(getKoin().get<PluginService>()[this].listenerBuilder)
    }

    @MinixDsl
    inner class ExtensionsBuilder {

        @MinixDsl
        @Suppress("UNCHECKED_CAST")
        inline infix fun <reified P : MinixPlugin> add(noinline builder: ExtensionUnit<P>) {
            getKoin().get<PluginService>()[this@MinixPlugin].extensions.add(builder as ExtensionUnit<MinixPlugin>)
        }
    }

    @MinixDsl
    inner class ListenerBuilder {

        @MinixDsl
        infix fun MinixPlugin.add(builder: ListenerUnit) {
            getKoin().get<PluginService>()[this@MinixPlugin].listeners += builder()
        }
    }
}
