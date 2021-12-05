---
title: Pdc
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.minecraft](../index.html)/[Pdc](index.html)



# Pdc



[jvm]\
class [Pdc](index.html) : [NbtWrapper](../-nbt-wrapper/index.html)



## Types


| Name | Summary |
|---|---|
| [Companion](-companion/index.html) | [jvm]<br>object [Companion](-companion/index.html) |


## Functions


| Name | Summary |
|---|---|
| [getString](get-string.html) | [jvm]<br>open override fun [getString](get-string.html)(itemStack: ItemStack, key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?<br>Gets the NBT tag based on a given key. |
| [removeTag](remove-tag.html) | [jvm]<br>open override fun [removeTag](remove-tag.html)(itemStack: ItemStack, key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?): ItemStack<br>Removes a tag from an ItemStack. |
| [setBoolean](set-boolean.html) | [jvm]<br>open override fun [setBoolean](set-boolean.html)(itemStack: ItemStack, key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, value: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): ItemStack<br>Sets a boolean to the ItemStack. Mainly used for setting an item to be unbreakable on older versions. |
| [setString](set-string.html) | [jvm]<br>open override fun [setString](set-string.html)(itemStack: ItemStack, key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?): ItemStack<br>Sets a String NBT tag to the an ItemStack. |

