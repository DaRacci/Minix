package dev.racci.minix.api.events.plugin

import dev.racci.minix.api.events.MinixCancellable
import dev.racci.minix.api.plugin.MinixPlugin

public expect class CaughtCoroutineExceptionEvent(
    plugin: MinixPlugin,
    err: Throwable
) : MinixPluginEvent, MinixCancellable
