package dev.racci.minix.data.utils

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.toPersistentMap
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlinx.serialization.Transient as SerializationTransient

/**
 * A helper class which when inherited will create a map of properties with type [R].
 * This map will allow searching via the given [KeyMode] format.
 *
 *
 *
 * @param R
 * @constructor Create empty Property finder
 */
public abstract class PropertyFinder<R> public constructor(
    @[Transient SerializationTransient]
    private val keyMode: KeyMode = KeyMode.CAPITAL_TO_DOT
) {

    @[Transient SerializationTransient]
    public val propertyMap: PersistentMap<String, KProperty1<Any, R>>

    public val keys: ImmutableSet<String> get() = propertyMap.keys

    init {
        val properties = this::class.declaredMemberProperties.filterIsInstance<KProperty1<Any, R>>()
        propertyMap = properties.associateBy { property -> keyMode.format(property.name) }.toPersistentMap()
    }

    public open operator fun get(
        key: String,
        convertFirst: Boolean = true
    ): R {
        if (!convertFirst) {
            val prop = propertyMap[key]
            if (prop != null) return prop.get(this)
        }

        return propertyMap[keyMode.format(key)]?.get(this) ?: throw IllegalArgumentException("Property $key not found")
    }

    public enum class KeyMode {
        /**
         * Before each Capital letter, there will be a '.' added and the lowercase character.
         * ```
         * ExampleProperty -> example.property
         * ```
         */
        CAPITAL_TO_DOT {
            override fun format(string: String): String = buildString {
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
            override fun format(string: String): String = string.uppercase()
        },

        /**
         * Uses the key as is without any changes.
         * ```
         * exAmple.proPerty -> exAmple.proPerty
         */
        RAW {
            override fun format(string: String): String = string
        };

        public abstract fun format(string: String): String
    }
}
