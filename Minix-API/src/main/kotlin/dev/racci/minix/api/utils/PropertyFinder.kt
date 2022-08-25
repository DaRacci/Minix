package dev.racci.minix.api.utils

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.toPersistentMap
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

abstract class PropertyFinder<R> {
    @Transient
    @kotlinx.serialization.Transient
    private val propertyMap: PersistentMap<String, KProperty1<Any, R>>

    operator fun get(key: String): R = propertyMap[key]?.get(this) ?: throw IllegalArgumentException("No property found for $key")

    init {
        val properties = this::class.declaredMemberProperties.filterIsInstance<KProperty1<Any, R>>()
        propertyMap = properties.associateBy { property ->
            buildString {
                for ((index, char) in property.name.withIndex()) {
                    if (index == 0 || char.isLowerCase()) {
                        append(char)
                        continue
                    }

                    append('.').append(char.lowercaseChar())
                }
            }
        }.toPersistentMap()
    }
}
