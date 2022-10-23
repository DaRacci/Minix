package dev.racci.minix.data.serializers.kotlin.recipes

import dev.racci.minix.data.extensions.bukkit
import dev.racci.minix.data.serializers.kotlin.minecraft.ItemStackSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@Serializable
@ConfigSerializable
@SerialName("shaped")
public class ShapedRecipeIngredients(
    public val items: Map<String, @Serializable(with = ItemStackSerializer::class) ItemStack>,
    public val configuration: String = ""
) : SerializableRecipeIngredients() {

    override fun toRecipe(
        key: Key,
        result: ItemStack,
        group: String
    ): Recipe {
        val recipe = ShapedRecipe(key.bukkit, result)

        recipe.shape(*configuration.replace("|", "").split("\n").toTypedArray())

        recipe.group = group

        items.forEach { (key, ingredient) ->
//            TODO
//            if (ingredient.tag != "") {
//                recipe.setIngredient(
//                    key[0],
//                    RecipeChoice.MaterialChoice(
//                        Bukkit.getTag(
//                            Tag.REGISTRY_BLOCKS,
//                            NamespacedKey.minecraft(ingredient.tag),
//                            Material::class.java
//                        )!!
//                    )
//                )
//            } else {
            recipe.setIngredient(key[0], RecipeChoice.ExactChoice(ingredient))
//            }
        }

        return recipe
    }
}
