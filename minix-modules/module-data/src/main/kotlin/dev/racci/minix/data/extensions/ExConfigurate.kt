package dev.racci.minix.data.extensions // ktlint-disable filename

import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.serialize.SerializationException

public fun ConfigurationNode.nonVirtualNode(
    vararg path: Any
): Result<ConfigurationNode> = if (!this.hasChild(*path)) {
    Result.failure(SerializationException("Field " + path.joinToString("") + " was not present in node"))
} else Result.success(this.node(*path))
