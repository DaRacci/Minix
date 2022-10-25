package dev.racci.minix.data.extensions

import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey

public val Key.bukkit: NamespacedKey get() = NamespacedKey(this.namespace(), this.value())
