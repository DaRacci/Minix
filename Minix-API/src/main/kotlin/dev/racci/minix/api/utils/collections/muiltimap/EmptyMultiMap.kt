package dev.racci.minix.api.utils.collections.muiltimap

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf

object EmptyMultiMap : MultiMap<Any?, Nothing> {
    override fun equals(other: Any?): Boolean = other is Map<*, *> && other.isEmpty()
    override fun hashCode(): Int = 0
    override fun toString(): String = "{}"

    override val size: Int get() = 0
    override val isEmpty: Boolean get() = true
    override val keys: ImmutableSet<Any?> = persistentSetOf()
    override val entries: ImmutableSet<Map.Entry<Any?, Collection<Nothing>>> = persistentSetOf()
    override val values: ImmutableSet<Collection<Nothing>> get() = persistentSetOf()

    override fun get(key: Any?): MutableCollection<Nothing>? = null

    override fun size(): Int = 0

    override fun containsKey(key: Any?): Boolean = false

    override fun forEach(block: (Any?, Collection<Nothing>?) -> Unit) = Unit

    override fun forEachIndexed(block: (index: Int, Any?, Collection<Nothing>?) -> Unit) = Unit
}
