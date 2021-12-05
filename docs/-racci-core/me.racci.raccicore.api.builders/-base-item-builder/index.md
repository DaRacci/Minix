---
title: BaseItemBuilder
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.builders](../index.html)/[BaseItemBuilder](index.html)



# BaseItemBuilder



[jvm]\
abstract class [BaseItemBuilder](index.html)&lt;[T](index.html) : [BaseItemBuilder](index.html)&lt;[T](index.html)&gt;&gt;



## Functions


| Name | Summary |
|---|---|
| [addFlag](add-flag.html) | [jvm]<br>open fun [addFlag](add-flag.html)(vararg flags: ItemFlag): [T](index.html) |
| [booleanNBT](boolean-n-b-t.html) | [jvm]<br>open fun [booleanNBT](boolean-n-b-t.html)(vararg pair: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt;): [T](index.html) |
| [build](build.html) | [jvm]<br>open fun [build](build.html)(): ItemStack |
| [disenchant](disenchant.html) | [jvm]<br>open fun [disenchant](disenchant.html)(vararg enchants: Enchantment): [T](index.html) |
| [enchant](enchant.html) | [jvm]<br>open fun [enchant](enchant.html)(vararg enchants: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;Enchantment, [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt;): [T](index.html) |
| [glow](glow.html) | [jvm]<br>open fun [glow](glow.html)(boolean: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true): [T](index.html) |
| [lore](lore.html) | [jvm]<br>open fun [lore](lore.html)(vararg component: Component): [T](index.html)<br>open fun [lore](lore.html)(unit: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;.() -&gt; [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;) |
| [pdc](pdc.html) | [jvm]<br>open fun [pdc](pdc.html)(unit: PersistentDataContainer.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [T](index.html) |
| [removeFlag](remove-flag.html) | [jvm]<br>open fun [removeFlag](remove-flag.html)(vararg flags: ItemFlag): [T](index.html) |
| [removeNBT](remove-n-b-t.html) | [jvm]<br>open fun [removeNBT](remove-n-b-t.html)(vararg key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [T](index.html) |
| [stringNBT](string-n-b-t.html) | [jvm]<br>open fun [stringNBT](string-n-b-t.html)(vararg pair: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;): [T](index.html) |
| [unbreakable](unbreakable.html) | [jvm]<br>open fun [unbreakable](unbreakable.html)(unbreakable: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true): [T](index.html) |


## Properties


| Name | Summary |
|---|---|
| [amount](amount.html) | [jvm]<br>var [amount](amount.html): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [glow](glow.html) | [jvm]<br>var [glow](glow.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [itemStack](item-stack.html) | [jvm]<br>var [itemStack](item-stack.html): ItemStack |
| [lore](lore.html) | [jvm]<br>var [lore](lore.html): Component |
| [meta](meta.html) | [jvm]<br>var [meta](meta.html): ItemMeta |
| [model](model.html) | [jvm]<br>var [model](model.html): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [name](name.html) | [jvm]<br>var [name](name.html): @NullableComponent? |
| [nbt](nbt.html) | [jvm]<br>var [nbt](nbt.html): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt; |
| [pdc](pdc.html) | [jvm]<br>val [pdc](pdc.html): @NotNullPersistentDataContainer |
| [unbreakable](unbreakable.html) | [jvm]<br>var [unbreakable](unbreakable.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |


## Inheritors


| Name |
|---|
| [BannerBuilder](../-banner-builder/index.html) |
| [BookBuilder](../-book-builder/index.html) |
| [FireworkBuilder](../-firework-builder/index.html) |
| [HeadBuilder](../-head-builder/index.html) |
| [ItemBuilder](../-item-builder/index.html) |
| [MapBuilder](../-map-builder/index.html) |

