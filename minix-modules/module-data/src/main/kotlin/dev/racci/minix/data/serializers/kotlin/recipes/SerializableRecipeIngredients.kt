package dev.racci.minix.data.serializers.kotlin.recipes

import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@Serializable
@ConfigSerializable
public sealed class SerializableRecipeIngredients {

    public abstract fun toRecipe(
        key: Key,
        result: ItemStack,
        group: String = ""
    ): Recipe
}
