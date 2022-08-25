package dev.racci.minix.api.data

import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.PropertyFinder
import dev.racci.minix.api.utils.adventure.PartialComponent
import dev.racci.minix.api.utils.safeCast
import dev.racci.minix.api.utils.unsafeCast
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.typeOf

@ConfigSerializable
abstract class LangConfig<P : MinixPlugin> : MinixConfig<P>() {

    open val prefixes: Map<String, String> = mapOf()

    override fun load() {
        val map = prefixes.mapKeys {
            if (!it.key.matches(prefixRegex)) "<prefix_${it.key}>" else it.key
        }

        val initialNested = getNested(this)
        val queue = ArrayDeque(initialNested)
        while (queue.isNotEmpty()) {
            val (instance, property) = queue.removeFirst()

            val propInstance = try {
                property.get(instance)
            } catch (e: ClassCastException) {
                continue
            }

            val nested = getNested(propInstance)
            if (nested.isNotEmpty()) {
                queue.addAll(nested)
            }

            propInstance::class.declaredMemberProperties
                .filterIsInstance<KProperty1<PropertyFinder<PartialComponent>, PartialComponent>>()
                .forEach {
                    try {
                        it.get(propInstance).formatRaw(map)
                    } catch (e: ClassCastException) {
                        return@forEach
                    }
                }
        }

        super.handleLoad()
    }

    operator fun get(key: String, vararg placeholder: Pair<String, () -> Any>): Component {
        val keys = key.split(".")
        if (keys.size <= 1) return MiniMessage.miniMessage().deserialize("Invalid key: $key")

        val prop = LangConfig::class.declaredMemberProperties.find { it.name == keys[0] } ?: return MiniMessage.miniMessage().deserialize("No Property found for $key")
        val value = prop.get(this).safeCast<PropertyFinder<PartialComponent>>() ?: return MiniMessage.miniMessage().deserialize("$key's return type is not a property finder class.")

        return value[key.substringAfter('.')].get(*placeholder)
    }

    private fun <T : Any> getNested(instance: T): List<Pair<Any, KProperty1<Any, PropertyFinder<PartialComponent>>>> {
        return instance::class.declaredMemberProperties
            .filter { it.returnType.isSubtypeOf(typeOf<PropertyFinder<*>>()) }
            .filterIsInstance<KProperty1<T, PropertyFinder<PartialComponent>>>()
            .map { instance to it }.unsafeCast()
    }

    companion object {
        private val prefixRegex = Regex("<prefix_(.*)>")
    }
}
