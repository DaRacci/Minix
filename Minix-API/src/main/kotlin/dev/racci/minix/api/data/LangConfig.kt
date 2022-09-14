package dev.racci.minix.api.data

import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.PropertyFinder
import dev.racci.minix.api.utils.adventure.PartialComponent
import dev.racci.minix.api.utils.safeCast
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

@ConfigSerializable
abstract class LangConfig<P : MinixPlugin> : MinixConfig<P>(false) {

    open val prefixes: Map<String, String> = mapOf()

    override fun load() {
        super.load()
        val map = prefixes.mapKeys {
            if (it.key.startsWith("<prefix_") && it.key.endsWith(">")) {
                it.key
            } else "<prefix_${it.key}>"
        }

        this.onNestedInstance<PropertyFinder<PartialComponent>> {
            this::class.declaredMemberProperties
                .filterIsInstance<KProperty1<PropertyFinder<PartialComponent>, PartialComponent>>()
                .map { it.getter.call(this) }
                .forEach { it.formatRaw(map) }
        }
    }

    operator fun get(key: String, vararg placeholder: Pair<String, () -> Any>): Component {
        val keys = key.split(".")
        if (keys.size <= 1) return MiniMessage.miniMessage().deserialize("Invalid key: $key")

        val prop = LangConfig::class.declaredMemberProperties.find { it.name == keys[0] } ?: return MiniMessage.miniMessage().deserialize("No Property found for $key")
        val value = prop.get(this).safeCast<PropertyFinder<PartialComponent>>() ?: return MiniMessage.miniMessage().deserialize("$key's return type is not a property finder class.")

        return value[key.substringAfter('.')].get(*placeholder)
    }

    @ConfigSerializable
    open class InnerLang : PropertyFinder<PartialComponent>(), InnerConfig by InnerConfig.Default()
}
