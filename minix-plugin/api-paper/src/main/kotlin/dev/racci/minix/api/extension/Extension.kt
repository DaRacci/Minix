package dev.racci.minix.api.extension

import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.PluginService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

public actual abstract class Extension<P : MinixPlugin> : PlatformIndependentExtension<P>() {
    public fun sync(
        block: suspend CoroutineScope.() -> Unit
    ): Job = PluginService.coroutineSession[plugin].launch(plugin.minecraftContext, this.supervisor, block = block)
}
