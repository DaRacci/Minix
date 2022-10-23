package dev.racci.minix.data.serializers.kotlin.recipes

import dev.racci.minix.data.extensions.bukkit
import dev.racci.minix.data.serializers.kotlin.minecraft.ItemStackSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import org.bukkit.inventory.FurnaceRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@Serializable
@ConfigSerializable
@SerialName("furnace")
public class FurnaceRecipeIngredients(
    @Serializable(with = ItemStackSerializer::class)
    public val input: ItemStack,
    public val experience: Float,
    public val cookingTime: Int
) : SerializableRecipeIngredients() {

    public override fun toRecipe(
        key: Key,
        result: ItemStack,
        group: String
    ): Recipe {
        val recipe = FurnaceRecipe(key.bukkit, result, RecipeChoice.ExactChoice(input), experience, cookingTime)

        recipe.group = group

        return recipe
    }
}
