package dev.racci.minix.api.plugin

import org.bstats.charts.CustomChart

public expect class Metrics internal constructor(plugin: MinixPlugin) {
    public val metricsID: Int
    public val enabled: Boolean

    public fun addCustomChart(chart: CustomChart)

    public suspend fun initialize()
}
