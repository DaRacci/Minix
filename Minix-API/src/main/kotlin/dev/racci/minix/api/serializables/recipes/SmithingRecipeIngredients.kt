@file:Suppress("Unused", "MemberVisibilityCanBePrivate")

package dev.racci.minix.api.serializables.recipes

import dev.racci.minix.api.serializables.SerializableItemStack
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.SmithingRecipe
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@Serializable
@ConfigSerializable
@SerialName("smithing")
class SmithingRecipeIngredients(
    val input: SerializableItemStack,
    val addition: SerializableItemStack,
) : SerializableRecipeIngredients() {

    override fun toRecipe(
        key: NamespacedKey,
        result: ItemStack,
        group: String,
    ): Recipe = SmithingRecipe(
        key,
        result,
        RecipeChoice.ExactChoice(input.toItemStack()),
        RecipeChoice.ExactChoice(addition.toItemStack()),
        false
    )
}
