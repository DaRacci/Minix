package dev.racci.minix.api.extension

import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.flowbus.receiver.EventReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

public actual abstract class Extension<P : MinixPlugin> : PlatformIndependentExtension<P>(), EventReceiver by getKoin().get() {
    public override fun sync(
        block: suspend CoroutineScope.() -> Unit
    ): Job = PluginService.coroutineSession[plugin].launch(plugin.minecraftContext, this.supervisor, block = block)
}
