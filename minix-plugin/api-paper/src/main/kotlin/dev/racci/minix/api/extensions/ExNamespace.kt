package dev.racci.minix.api.extensions // ktlint-disable filename

import org.bukkit.NamespacedKey

public fun String.toNamespacedKey(): NamespacedKey {
    val split = split(':')
    require(split.size == 2) { "Malformed key: $this, a key must only contain a singular : that splits the namespaced from the key." }
    return NamespacedKey(split[0], split[1])
}
