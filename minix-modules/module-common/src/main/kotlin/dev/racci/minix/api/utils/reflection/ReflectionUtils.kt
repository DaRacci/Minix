package dev.racci.minix.api.utils.reflection

import arrow.core.Validated
import dev.racci.minix.api.extensions.reflection.accessGet
import dev.racci.minix.api.extensions.reflection.accessSet
import dev.racci.minix.api.extensions.reflection.castOrThrow
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

public object ReflectionUtils {

    /**
     * Safely gets the class with this name wrapped in a [Validated].
     * The throwable will be of type [ClassNotFoundException] or [ClassCastException] if [T] is not assignable from the class.
     *
     * @param name The fully qualified name of the class.
     * @param T The type of the class.
     * @return A [KClass] reference to the class.
     */
    public fun <T : Any> classForName(name: String): Validated<Throwable, KClass<T>> {
        return Validated.catch { Class.forName(name).castOrThrow<Class<T>>().kotlin }
    }

    /**
     * Reads the properties of an instance through reflection,
     * and constructs a new instance of the same type.
     * If this class contains immutable properties that cannot be changed through reflection these cannot be cloned and will be ignored.
     *
     * @param replaceProperties A map of properties to replace in the new instance.
     * @param T The type of the instance.
     * @return The new cloned instance.
     */
    public suspend inline fun <T : Any> clone(
        obj: T,
        replaceProperties: Map<KProperty1<T, *>, Any> = emptyMap()
    ): T = obj::class.run {
        val consParams = primaryConstructor?.parameters ?: error("No primary constructor found for class $qualifiedName")
        val mutableProperties = memberProperties.filterIsInstance<KMutableProperty1<T, Any?>>()
        val allValues = memberProperties
            .filter { it in mutableProperties || it.name in consParams.map(KParameter::name) }
            .associate { it.name to (replaceProperties[it] ?: it.castOrThrow<KProperty1<T, Any?>>().accessGet(obj)) }

        primaryConstructor!!.callBy(consParams.associateWith { allValues[it.name] }).also { newInstance ->
            for (prop in mutableProperties) {
                prop.accessSet(newInstance, allValues[prop.name])
            }
        }
    }
}
