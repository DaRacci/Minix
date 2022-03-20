@file:Suppress("Unused", "MemberVisibilityCanBePrivate")

package dev.racci.minix.api.serializables.recipes

import dev.racci.minix.api.extensions.toNamespacedKey
import dev.racci.minix.api.serializables.SerializableItemStack
import kotlinx.serialization.Serializable
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@Serializable
@ConfigSerializable
class SerializableRecipe(
    val key: String,
    val ingredients: SerializableRecipeIngredients,
    val result: SerializableItemStack,
    val group: String = "",
) {

    fun toCraftingRecipe() =
        ingredients.toRecipe(key.toNamespacedKey(), result.toItemStack(), group)
}
