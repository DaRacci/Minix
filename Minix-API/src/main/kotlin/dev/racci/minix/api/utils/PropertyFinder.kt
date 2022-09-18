package dev.racci.minix.api.utils

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.toPersistentMap
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

abstract class PropertyFinder<R> {
    @Transient
    @kotlinx.serialization.Transient
    val propertyMap: PersistentMap<String, KProperty1<Any, R>>

    open operator fun get(key: String): R {
        return propertyMap[formatString(key)]?.get(this) ?: throw IllegalArgumentException("Property $key not found")
    }

    init {
        val properties = this::class.declaredMemberProperties.filterIsInstance<KProperty1<Any, R>>()
        propertyMap = properties.associateBy { property -> formatString(property.name) }.toPersistentMap()
    }

    companion object {
        fun formatString(string: String) = buildString {
            for ((index, char) in string.withIndex()) {
                if (index == 0 || char.isLowerCase()) {
                    append(char)
                    continue
                }

                append('.').append(char.lowercaseChar())
            }
        }
    }
}
