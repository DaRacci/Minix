package dev.racci.minix.api.events.plugin

import dev.racci.minix.api.events.MinixCancellable
import dev.racci.minix.api.plugin.MinixPlugin

public actual class CaughtCoroutineExceptionEvent actual constructor(
    plugin: MinixPlugin,
    err: Throwable
) : MinixPluginEvent(plugin, true), MinixCancellable {
    public override var actualCancelled: Boolean = false
}
