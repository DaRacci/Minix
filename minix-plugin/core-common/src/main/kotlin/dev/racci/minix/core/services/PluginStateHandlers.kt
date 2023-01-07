package dev.racci.minix.core.services

import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.events.plugin.MinixPluginStateEvent
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.core.plugin.Minix
import dev.racci.minix.flowbus.subscribe

@MappedExtension
public class PluginStateHandlers : Extension<Minix>() {

    public override suspend fun handleLoad() {
        subscribe<MinixPluginStateEvent> {
            when (this.state) {
                MinixPluginStateEvent.State.LOAD -> bStatsRegister(this.plugin)
                MinixPluginStateEvent.State.RELOAD -> bStatsRegister(this.plugin)
                else -> { /* no-op */
                }
            }
        }
    }

    private suspend fun bStatsRegister(plugin: MinixPlugin) {
        val metrics = plugin.metrics
        if (metrics.metricsID == -1) return logger.debug { "Skipping bStats registration for ${plugin.value}" }

        metrics.initialize()
    }
}
