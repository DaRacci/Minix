package dev.racci.minix.core.plugin

import dev.racci.minix.api.plugin.MinixPlugin
import org.bukkit.plugin.Plugin
import java.lang.ref.WeakReference

public expect class Minix internal constructor(initPlugin: WeakReference<Plugin>) : MinixPlugin {
    protected suspend fun startKoin()

    protected suspend fun startSentry()
}
