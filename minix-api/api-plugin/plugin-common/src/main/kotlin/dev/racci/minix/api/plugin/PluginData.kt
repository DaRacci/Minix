package dev.racci.minix.api.plugin

import dev.racci.minix.api.logger.MinixLogger
import dev.racci.minix.api.utils.Version

public expect class PluginData {

    public val name: String

    public val version: Version

    public val logger: MinixLogger

    public val metrics: Metrics

    override fun toString(): String

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}
