package dev.racci.minix.api.extensions

import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataHolder

public inline fun PersistentDataHolder.pdc(
    block: PersistentDataContainer.() -> Unit
): PDC = persistentDataContainer.also(block)

public val PersistentDataHolder.pdc: PDC get() = this.persistentDataContainer
