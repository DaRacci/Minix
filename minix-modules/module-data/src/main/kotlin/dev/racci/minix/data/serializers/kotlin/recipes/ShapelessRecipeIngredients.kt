@file:Suppress("Unused", "MemberVisibilityCanBePrivate")

package dev.racci.minix.data.serializers.kotlin.recipes

import dev.racci.minix.data.extensions.bukkit
import dev.racci.minix.data.serializers.kotlin.minecraft.ItemStackSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapelessRecipe
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@Serializable
@ConfigSerializable
@SerialName("shapeless")
public class ShapelessRecipeIngredients(
    public val items: List<@Serializable(with = ItemStackSerializer::class) ItemStack>
) : SerializableRecipeIngredients() {

    override fun toRecipe(
        key: Key,
        result: ItemStack,
        group: String
    ): Recipe {
        val recipe = ShapelessRecipe(key.bukkit, result)
        recipe.group = group
        items.forEach { ingredient ->
//            TODO
//            if (ingredient.tag != "") {
//                recipe.addIngredient(
//                    RecipeChoice.MaterialChoice(
//                        Bukkit.getTag(
//                            Tag.REGISTRY_BLOCKS,
//                            NamespacedKey.minecraft(ingredient.tag),
//                            Material::class.java
//                        )!!
//                    )
//                )
//            } else
            recipe.addIngredient(RecipeChoice.ExactChoice(ingredient))
        }
        return recipe
    }
}
