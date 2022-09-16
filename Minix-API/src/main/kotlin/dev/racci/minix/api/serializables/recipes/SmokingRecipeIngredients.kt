@file:Suppress("Unused", "MemberVisibilityCanBePrivate")

package dev.racci.minix.api.serializables.recipes

import dev.racci.minix.api.serializables.SerializableItemStack
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.SmokingRecipe
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@Serializable
@ConfigSerializable
@SerialName("smoking")
class SmokingRecipeIngredients(
    val input: SerializableItemStack,
    val experience: Float,
    val cookingTime: Int
) : SerializableRecipeIngredients() {

    override fun toRecipe(
        key: NamespacedKey,
        result: ItemStack,
        group: String
    ): Recipe {
        val recipe = SmokingRecipe(key, result, RecipeChoice.ExactChoice(input.toItemStack()), experience, cookingTime)

        recipe.group = group

        return recipe
    }
}
