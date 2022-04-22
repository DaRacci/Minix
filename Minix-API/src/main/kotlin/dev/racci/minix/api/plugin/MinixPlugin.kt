package dev.racci.minix.api.plugin

import dev.racci.minix.api.annotations.MappedPlugin
import dev.racci.minix.api.annotations.MinixDsl
import dev.racci.minix.api.data.PluginUpdater
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extension.ExtensionStateEvent
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.safeCast
import kotlinx.coroutines.flow.filterIsInstance
import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval
import org.koin.core.component.get
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubclassOf

/**
 * The superclass replacing [JavaPlugin],
 * This class provides many more features and allows implementation into the
 * systems of Minix.
 */
@Suppress("LeakingThis")
abstract class MinixPlugin : JavaPlugin(), SusPlugin {
    @get:ApiStatus.Internal // Public to be used within the plugin service impl
    val annotation by lazy { this::class.findAnnotation<MappedPlugin>() }

    override val bStatsId: Int? by lazy { annotation?.bStatsId.takeIf { it != -1 } }
    override val bindToKClass: KClass<out MinixPlugin>? by lazy { annotation?.bindToKClass.takeIf { it?.isSubclassOf(MinixPlugin::class) == true }.safeCast() }

    val log: MinixLogger get() = get<PluginService>()[this].log
    val metrics: Metrics? get() = get<PluginService>()[this].metrics

    open val updater: PluginUpdater? = null

    @ApiStatus.Internal
    @ApiStatus.NonExtendable
    override fun onEnable() {
        get<PluginService>().startPlugin(this)
    }

    @ApiStatus.Internal
    @ApiStatus.NonExtendable
    override fun onDisable() {
        get<PluginService>().unloadPlugin(this)
    }

    @ApiStatus.Internal
    @ApiStatus.NonExtendable
    override fun onLoad() {
        get<PluginService>().loadPlugin(this)
    }

    @ApiStatus.Internal
    @ApiStatus.NonExtendable
    suspend inline fun send(event: ExtensionStateEvent) {
        get<PluginService>()[this].extensionEvents.emit(event)
    }

    // TODO: Is this how this works?
    suspend inline fun <reified T : Extension<P>, P : MinixPlugin> extensionEvent(crossinline block: suspend T.() -> Unit) {
        get<PluginService>()[this].extensionEvents
            .filterIsInstance<T>()
            .collect { block.invoke(it) }
    }

    @MinixDsl
    protected suspend fun extensions(builder: suspend ExtensionsBuilder.() -> Unit) {
        builder(get<PluginService>()[this].extensionsBuilder)
    }

    @MinixDsl
    @Deprecated("Use extensions() instead", ReplaceWith("extensions(builder)"))
    @ScheduledForRemoval(inVersion = "3.0.0")
    protected suspend fun listeners(builder: suspend ListenerBuilder.() -> Unit) {
        builder(get<PluginService>()[this].listenerBuilder)
    }

    @MinixDsl
    inner class ExtensionsBuilder {

        @MinixDsl
        @Suppress("UNCHECKED_CAST")
        inline infix fun <reified P : MinixPlugin> add(noinline builder: ExtensionUnit<P>) {
            get<PluginService>()[this@MinixPlugin].extensions.add(builder as ExtensionUnit<MinixPlugin>)
        }
    }

    @MinixDsl
    @Deprecated("Use ExtensionBuilder() instead", ReplaceWith("ExtensionBuilder()"))
    @ScheduledForRemoval(inVersion = "3.0.0")
    inner class ListenerBuilder {

        @MinixDsl
        @Deprecated("Use extensions instead", ReplaceWith("extensions(builder)"))
        @ScheduledForRemoval(inVersion = "3.0.0")
        infix fun MinixPlugin.add(builder: ListenerUnit) {
            get<PluginService>()[this@MinixPlugin].listeners += builder()
        }
    }
}
