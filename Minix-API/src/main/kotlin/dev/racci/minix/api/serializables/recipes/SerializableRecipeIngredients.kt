@file:Suppress("Unused")

package dev.racci.minix.api.serializables.recipes

import kotlinx.serialization.Serializable
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@Serializable
@ConfigSerializable
sealed class SerializableRecipeIngredients {

    abstract fun toRecipe(
        key: NamespacedKey,
        result: ItemStack,
        group: String = "",
    ): Recipe
}
