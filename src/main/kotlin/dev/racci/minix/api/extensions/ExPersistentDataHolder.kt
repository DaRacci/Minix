package dev.racci.minix.api.extensions

import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataHolder

inline fun PersistentDataHolder.pdc(
    block: PersistentDataContainer.() -> Unit
) = persistentDataContainer.also(block)

val PersistentDataHolder.pdc get() = this.persistentDataContainer
