@file:Suppress("Unused", "MemberVisibilityCanBePrivate")

package dev.racci.minix.api.serializables.recipes

import dev.racci.minix.api.serializables.SerializableItemStack
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Tag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapelessRecipe
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@Serializable
@ConfigSerializable
@SerialName("shapeless")
class ShapelessRecipeIngredients(
    val items: List<SerializableItemStack>,
) : SerializableRecipeIngredients() {

    override fun toRecipe(
        key: NamespacedKey,
        result: ItemStack,
        group: String,
    ): Recipe {
        val recipe = ShapelessRecipe(key, result)
        recipe.group = group
        items.forEach { ingredient ->
            if (ingredient.tag !== "") {
                recipe.addIngredient(
                    RecipeChoice.MaterialChoice(
                        Bukkit.getTag(
                            Tag.REGISTRY_BLOCKS,
                            NamespacedKey.minecraft(ingredient.tag),
                            Material::class.java
                        )!!
                    )
                )
            } else recipe.addIngredient(RecipeChoice.ExactChoice(ingredient.toItemStack()))
        }
        return recipe
    }
}
