---
title: ItemBuilder
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.builders](../index.html)/[ItemBuilder](index.html)



# ItemBuilder



[jvm]\
class [ItemBuilder](index.html) : [BaseItemBuilder](../-base-item-builder/index.html)&lt;[ItemBuilder](index.html)&gt;



## Types


| Name | Summary |
|---|---|
| [Companion](-companion/index.html) | [jvm]<br>object [Companion](-companion/index.html) |


## Functions


| Name | Summary |
|---|---|
| [addFlag](../-base-item-builder/add-flag.html) | [jvm]<br>open fun [addFlag](../-base-item-builder/add-flag.html)(vararg flags: ItemFlag): [ItemBuilder](index.html) |
| [booleanNBT](../-base-item-builder/boolean-n-b-t.html) | [jvm]<br>open fun [booleanNBT](../-base-item-builder/boolean-n-b-t.html)(vararg pair: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt;): [ItemBuilder](index.html) |
| [build](../-base-item-builder/build.html) | [jvm]<br>open fun [build](../-base-item-builder/build.html)(): ItemStack |
| [disenchant](../-base-item-builder/disenchant.html) | [jvm]<br>open fun [disenchant](../-base-item-builder/disenchant.html)(vararg enchants: Enchantment): [ItemBuilder](index.html) |
| [enchant](../-base-item-builder/enchant.html) | [jvm]<br>open fun [enchant](../-base-item-builder/enchant.html)(vararg enchants: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;Enchantment, [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt;): [ItemBuilder](index.html) |
| [glow](../-base-item-builder/glow.html) | [jvm]<br>open fun [glow](../-base-item-builder/glow.html)(boolean: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true): [ItemBuilder](index.html) |
| [lore](../-base-item-builder/lore.html) | [jvm]<br>open fun [lore](../-base-item-builder/lore.html)(vararg component: Component): [ItemBuilder](index.html)<br>open fun [lore](../-base-item-builder/lore.html)(unit: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;.() -&gt; [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;) |
| [pdc](../-base-item-builder/pdc.html) | [jvm]<br>open fun [pdc](../-base-item-builder/pdc.html)(unit: PersistentDataContainer.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [ItemBuilder](index.html) |
| [removeFlag](../-base-item-builder/remove-flag.html) | [jvm]<br>open fun [removeFlag](../-base-item-builder/remove-flag.html)(vararg flags: ItemFlag): [ItemBuilder](index.html) |
| [removeNBT](../-base-item-builder/remove-n-b-t.html) | [jvm]<br>open fun [removeNBT](../-base-item-builder/remove-n-b-t.html)(vararg key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [ItemBuilder](index.html) |
| [stringNBT](../-base-item-builder/string-n-b-t.html) | [jvm]<br>open fun [stringNBT](../-base-item-builder/string-n-b-t.html)(vararg pair: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;): [ItemBuilder](index.html) |
| [unbreakable](../-base-item-builder/unbreakable.html) | [jvm]<br>open fun [unbreakable](../-base-item-builder/unbreakable.html)(unbreakable: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true): [ItemBuilder](index.html) |


## Properties


| Name | Summary |
|---|---|
| [amount](../-base-item-builder/amount.html) | [jvm]<br>var [amount](../-base-item-builder/amount.html): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [glow](../-base-item-builder/glow.html) | [jvm]<br>var [glow](../-base-item-builder/glow.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [itemStack](../-base-item-builder/item-stack.html) | [jvm]<br>var [itemStack](../-base-item-builder/item-stack.html): ItemStack |
| [lore](../-base-item-builder/lore.html) | [jvm]<br>var [lore](../-base-item-builder/lore.html): Component |
| [meta](../-base-item-builder/meta.html) | [jvm]<br>var [meta](../-base-item-builder/meta.html): ItemMeta |
| [model](../-base-item-builder/model.html) | [jvm]<br>var [model](../-base-item-builder/model.html): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [name](../-base-item-builder/name.html) | [jvm]<br>var [name](../-base-item-builder/name.html): @NullableComponent? |
| [nbt](../-base-item-builder/nbt.html) | [jvm]<br>var [nbt](../-base-item-builder/nbt.html): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt; |
| [pdc](../-base-item-builder/pdc.html) | [jvm]<br>val [pdc](../-base-item-builder/pdc.html): @NotNullPersistentDataContainer |
| [unbreakable](../-base-item-builder/unbreakable.html) | [jvm]<br>var [unbreakable](../-base-item-builder/unbreakable.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

