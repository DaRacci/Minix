package dev.racci.minix.api.plugin

import dev.racci.minix.api.extensions.collections.clear
import org.bstats.bukkit.Metrics
import org.bstats.charts.CustomChart

public actual class Metrics internal actual constructor(
    private val plugin: MinixPlugin
) {
    private val charts: MutableList<CustomChart> = mutableListOf()
    private var metricsBacker: Metrics? = null

    public actual val metricsID: Int = -1
    public actual val enabled: Boolean get() = metricsID != -1 && metricsBacker != null

    public actual fun addCustomChart(chart: CustomChart) {
        if (enabled) metricsBacker!!.addCustomChart(chart) else charts.add(chart)
    }

    public actual suspend fun initialize() {
        if (metricsID == -1 || enabled) return

        metricsBacker = Metrics(plugin, metricsID).apply {
            charts.clear(this::addCustomChart)
        }
    }
}
