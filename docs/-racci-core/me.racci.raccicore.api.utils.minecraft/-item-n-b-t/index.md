//[RacciCore](../../../index.md)/[me.racci.raccicore.api.utils.minecraft](../index.md)/[ItemNBT](index.md)

# ItemNBT

[jvm]\
object [ItemNBT](index.md)

## Functions

| Name | Summary |
|---|---|
| [getString](get-string.md) | [jvm]<br>fun [getString](get-string.md)(itemStack: ItemStack, key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?<br>Gets the NBT tag based on a given key. |
| [removeTag](remove-tag.md) | [jvm]<br>fun [removeTag](remove-tag.md)(itemStack: ItemStack, key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): ItemStack?<br>Removes a tag from an ItemStack. |
| [setBoolean](set-boolean.md) | [jvm]<br>fun [setBoolean](set-boolean.md)(itemStack: ItemStack, key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): ItemStack?<br>Sets a boolean to the ItemStack. Mainly used for setting an item to be unbreakable on older versions. |
| [setString](set-string.md) | [jvm]<br>fun [setString](set-string.md)(itemStack: ItemStack, key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): ItemStack?<br>Sets an NBT tag to the an ItemStack. |
