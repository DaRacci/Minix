package dev.racci.minix.data.serializers.kotlin.recipes

import dev.racci.minix.data.extensions.bukkit
import dev.racci.minix.data.serializers.kotlin.minecraft.ItemStackSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.StonecuttingRecipe
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@Serializable
@ConfigSerializable
@SerialName("stonecutting")
public class StoneCuttingRecipeIngredients(
    @Serializable(with = ItemStackSerializer::class)
    public val input: ItemStack
) : SerializableRecipeIngredients() {

    override fun toRecipe(
        key: Key,
        result: ItemStack,
        group: String
    ): Recipe {
        val recipe = StonecuttingRecipe(key.bukkit, result, RecipeChoice.ExactChoice(input))

        recipe.group = group

        return recipe
    }
}
