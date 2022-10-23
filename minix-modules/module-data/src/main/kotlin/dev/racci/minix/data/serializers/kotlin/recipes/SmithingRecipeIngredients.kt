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
import org.bukkit.inventory.SmithingRecipe
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@Serializable
@ConfigSerializable
@SerialName("smithing")
public class SmithingRecipeIngredients(
    @Serializable(with = ItemStackSerializer::class)
    public val input: ItemStack,
    @Serializable(with = ItemStackSerializer::class)
    public val addition: ItemStack
) : SerializableRecipeIngredients() {

    override fun toRecipe(
        key: Key,
        result: ItemStack,
        group: String
    ): Recipe = SmithingRecipe(
        key.bukkit,
        result,
        RecipeChoice.ExactChoice(input),
        RecipeChoice.ExactChoice(addition),
        false
    )
}
