//[RacciCore](../../../index.md)/[me.racci.raccicore.builders](../index.md)/[BookBuilder](index.md)

# BookBuilder

[jvm]\
class [BookBuilder](index.md) : [BaseItemBuilder](../-base-item-builder/index.md)&lt;[BookBuilder](index.md)&gt;

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [addFlag](../-base-item-builder/add-flag.md) | [jvm]<br>open fun [addFlag](../-base-item-builder/add-flag.md)(vararg flags: ItemFlag): [BookBuilder](index.md) |
| [amount](../-base-item-builder/amount.md) | [jvm]<br>open fun [amount](../-base-item-builder/amount.md)(amount: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [BookBuilder](index.md) |
| [author](author.md) | [jvm]<br>fun [author](author.md)(author: Component): [BookBuilder](index.md) |
| [booleanNBT](../-base-item-builder/boolean-n-b-t.md) | [jvm]<br>open fun [booleanNBT](../-base-item-builder/boolean-n-b-t.md)(vararg pair: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt;): [BookBuilder](index.md) |
| [build](build.md) | [jvm]<br>open override fun [build](build.md)(): ItemStack |
| [disenchant](../-base-item-builder/disenchant.md) | [jvm]<br>open fun [disenchant](../-base-item-builder/disenchant.md)(vararg enchants: Enchantment): [BookBuilder](index.md) |
| [enchant](../-base-item-builder/enchant.md) | [jvm]<br>open fun [enchant](../-base-item-builder/enchant.md)(vararg enchants: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;Enchantment, [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt;): [BookBuilder](index.md) |
| [generation](generation.md) | [jvm]<br>fun [generation](generation.md)(generation: BookMeta.Generation): [BookBuilder](index.md) |
| [glow](../-base-item-builder/glow.md) | [jvm]<br>open fun [glow](../-base-item-builder/glow.md)(boolean: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true): [BookBuilder](index.md) |
| [lore](../-base-item-builder/lore.md) | [jvm]<br>~~open~~ ~~fun~~ [~~lore~~](../-base-item-builder/lore.md)~~(~~~~component~~~~:~~ [Function](https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;, [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;&gt;~~)~~~~:~~ [BookBuilder](index.md)<br>open fun [lore](../-base-item-builder/lore.md)(vararg component: Component): [BookBuilder](index.md)<br>open fun [lore](../-base-item-builder/lore.md)(unit: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;.() -&gt; [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;) |
| [model](../-base-item-builder/model.md) | [jvm]<br>open fun [model](../-base-item-builder/model.md)(modelData: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [BookBuilder](index.md) |
| [name](../-base-item-builder/name.md) | [jvm]<br>open fun [name](../-base-item-builder/name.md)(component: Component): [BookBuilder](index.md) |
| [page](page.md) | [jvm]<br>fun [page](page.md)(vararg pages: Component): [BookBuilder](index.md)<br>fun [page](page.md)(page: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), data: Component): [BookBuilder](index.md) |
| [pdc](../-base-item-builder/pdc.md) | [jvm]<br>open fun [pdc](../-base-item-builder/pdc.md)(unit: PersistentDataContainer.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [BookBuilder](index.md) |
| [removeFlag](../-base-item-builder/remove-flag.md) | [jvm]<br>open fun [removeFlag](../-base-item-builder/remove-flag.md)(vararg flags: ItemFlag): [BookBuilder](index.md) |
| [removeNBT](../-base-item-builder/remove-n-b-t.md) | [jvm]<br>open fun [removeNBT](../-base-item-builder/remove-n-b-t.md)(vararg key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [BookBuilder](index.md) |
| [stringNBT](../-base-item-builder/string-n-b-t.md) | [jvm]<br>open fun [stringNBT](../-base-item-builder/string-n-b-t.md)(vararg pair: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;): [BookBuilder](index.md) |
| [title](title.md) | [jvm]<br>fun [title](title.md)(title: Component): [BookBuilder](index.md) |
| [unbreakable](../-base-item-builder/unbreakable.md) | [jvm]<br>open fun [unbreakable](../-base-item-builder/unbreakable.md)(unbreakable: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true): [BookBuilder](index.md) |

## Properties

| Name | Summary |
|---|---|
| [amount](../-base-item-builder/amount.md) | [jvm]<br>var [amount](../-base-item-builder/amount.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [bItemStack](b-item-stack.md) | [jvm]<br>var [bItemStack](b-item-stack.md): @NotNullItemStack |
| [glow](../-base-item-builder/glow.md) | [jvm]<br>var [glow](../-base-item-builder/glow.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [itemStack](../-base-item-builder/item-stack.md) | [jvm]<br>var [itemStack](../-base-item-builder/item-stack.md): ItemStack |
| [lore](../-base-item-builder/lore.md) | [jvm]<br>var [lore](../-base-item-builder/lore.md): Component |
| [meta](../-base-item-builder/meta.md) | [jvm]<br>var [meta](../-base-item-builder/meta.md): ItemMeta |
| [name](../-base-item-builder/name.md) | [jvm]<br>var [name](../-base-item-builder/name.md): @NullableComponent? |
| [nbt](../-base-item-builder/nbt.md) | [jvm]<br>var [nbt](../-base-item-builder/nbt.md): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt; |
| [unbreakable](../-base-item-builder/unbreakable.md) | [jvm]<br>var [unbreakable](../-base-item-builder/unbreakable.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
