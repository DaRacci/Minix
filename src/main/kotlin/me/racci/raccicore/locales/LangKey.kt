package me.racci.raccicore.locales

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger


class LangKey(
    val key: String,
): LangKeyProvider {

    private val id      = counter.getAndIncrement()

    override val langKey: LangKey get() = this
    override fun equals(other: Any?) = this === other
    override fun hashCode() = id

    companion object {

        private val counter = AtomicInteger(1)
        private val keyMap: MutableMap<String, LangKey> = ConcurrentHashMap()

        fun of(key: String) =
                keyMap.computeIfAbsent(key.lowercase().intern()) {LangKey(key)}

    }

}