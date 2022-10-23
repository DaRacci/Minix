package dev.racci.minix.data.serializers.kotlin.recipes

import dev.racci.minix.data.serializers.kotlin.minecraft.ItemStackSerializer
import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@Serializable
@ConfigSerializable
public class SerializableRecipe(
    public val key: Key,
    public val ingredients: SerializableRecipeIngredients,
    @Serializable(with = ItemStackSerializer::class)
    public val result: ItemStack,
    public val group: String = ""
) {

    public fun toCraftingRecipe(): Recipe = ingredients.toRecipe(key, result, group)
}
