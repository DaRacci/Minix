package dev.racci.minix.api.utils

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.toPersistentMap
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

abstract class PropertyFinder<R> {
    @Transient
    private val keyMode: KeyMode

    constructor(keyMode: KeyMode = KeyMode.CAPITAL_TO_DOT) {
        this.keyMode = keyMode

        val properties = this::class.declaredMemberProperties.filterIsInstance<KProperty1<Any, R>>()
        propertyMap = properties.associateBy { property -> keyMode.format(property.name) }.toPersistentMap()
    }

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("Use the new constructor instead")
    constructor() : this(KeyMode.CAPITAL_TO_DOT)

    @Transient
    @kotlinx.serialization.Transient
    val propertyMap: PersistentMap<String, KProperty1<Any, R>>

    val keys: ImmutableSet<String> get() = propertyMap.keys

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("Use the new constructor instead")
    operator fun get(key: String): R = get(key, true)

    open operator fun get(
        key: String,
        convertFirst: Boolean = true
    ): R {
        if (!convertFirst) {
            val prop = propertyMap[key]
            if (prop != null) return prop.get(this)
        }

        return propertyMap[keyMode.format(key)]?.get(this) ?: throw IllegalArgumentException("Property $key not found")
    }

    enum class KeyMode {
        /**
         * Before each Capital letter, there will be a '.' added and the lowercase character.
         * ```
         * ExampleProperty -> example.property
         * ```
         */
        CAPITAL_TO_DOT {
            override fun format(string: String) = buildString {
                for ((index, char) in string.withIndex()) {
                    if (index == 0 || char.isLowerCase() || !char.isLetter()) {
                        append(char)
                        continue
                    }

                    if (lastOrNull() != null && last() != '.') {
                        append('.')
                    }

                    append(char.lowercaseChar())
                }
            }
        },

        /**
         * Converts the string to be completely capitalised.
         * ```
         * example.property -> EXAMPLE.PROPERTY
         * ```
         */
        CAPITALISED {
            override fun format(string: String) = string.uppercase()
        },

        /**
         * Uses the key as is without any changes.
         * ```
         * exAmple.proPerty -> exAmple.proPerty
         */
        RAW {
            override fun format(string: String) = string
        };

        abstract fun format(string: String): String
    }
}
