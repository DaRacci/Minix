package dev.racci.minix.api.extensions

import org.bukkit.NamespacedKey

fun String.toNamespacedKey(): NamespacedKey {
    val split = split(':')
    assert(split.size == 2) {
        "Malformed key: $this, a key must only contain a singular : that splits the namespaced from the key."
    }
    @Suppress("Deprecation") // lmao fuck you imma use what is meant to be an internal function
    return NamespacedKey(split[0], split[1])
}
