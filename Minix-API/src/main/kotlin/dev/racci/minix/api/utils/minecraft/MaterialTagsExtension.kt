package dev.racci.minix.api.utils.minecraft

import com.destroystokyo.paper.MaterialSetTag
import com.destroystokyo.paper.MaterialTags
import dev.racci.minix.api.utils.unsafeCast
import org.bukkit.Material
import org.bukkit.NamespacedKey
import kotlin.reflect.KFunction
import kotlin.reflect.full.staticFunctions
import kotlin.reflect.jvm.isAccessible

object MaterialTagsExtension {

    private val internalKeyFor: KFunction<NamespacedKey>

    private fun keyFor(key: String): NamespacedKey = internalKeyFor.call(key)

    init {
        MaterialTags::class.staticFunctions.first { it.name == "keyFor" }.let {
            it.isAccessible = true
            internalKeyFor = it.unsafeCast()
        }
    }

    val RAW_MEATS: MaterialSetTag = MaterialSetTag(keyFor("raw_meats")).add(
        Material.BEEF,
        Material.CHICKEN,
        Material.COD,
        Material.MUTTON,
        Material.PORKCHOP,
        Material.PUFFERFISH,
        Material.RABBIT,
        Material.RABBIT_STEW,
        Material.ROTTEN_FLESH,
        Material.SALMON,
        Material.SPIDER_EYE,
        Material.TROPICAL_FISH
    )

    val COOKED_MEATS: MaterialSetTag = MaterialSetTag(keyFor("cooked_meats")).add(
        Material.COOKED_BEEF,
        Material.COOKED_CHICKEN,
        Material.COOKED_COD,
        Material.COOKED_MUTTON,
        Material.COOKED_PORKCHOP,
        Material.COOKED_RABBIT,
        Material.COOKED_SALMON
    )

    val VEGETABLES: MaterialSetTag = MaterialSetTag(keyFor("vegetables")).add(
        Material.BAKED_POTATO,
        Material.BEETROOT,
        Material.BEETROOT_SOUP,
        Material.CARROT,
        Material.DRIED_KELP,
        Material.GOLDEN_CARROT,
        Material.MUSHROOM_STEW,
        Material.POISONOUS_POTATO,
        Material.POTATO,
        Material.SUSPICIOUS_STEW
    )

    val FRUITS: MaterialSetTag = MaterialSetTag(keyFor("fruits")).add(
        Material.APPLE,
        Material.GOLDEN_APPLE,
        Material.CHORUS_FRUIT,
        Material.ENCHANTED_GOLDEN_APPLE,
        Material.GLOW_BERRIES,
        Material.MELON_SLICE,
        Material.PUMPKIN_PIE,
        Material.SWEET_BERRIES
    )

    val CARBS: MaterialSetTag = MaterialSetTag(keyFor("carbs")).add(
        Material.BREAD,
        Material.COOKIE,
        Material.HONEY_BOTTLE
    )
}
