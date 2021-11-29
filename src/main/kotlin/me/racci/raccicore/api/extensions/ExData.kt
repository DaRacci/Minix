@file:Suppress("UNUSED", "UNCHECKED_CAST")
package me.racci.raccicore.api.extensions

import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataHolder

inline fun PersistentDataHolder.pdc(
    block: PersistentDataContainer.() -> Unit
) = persistentDataContainer.also(block)

val PersistentDataHolder.pdc get() = persistentDataContainer