//[RacciCore](../../../index.md)/[me.racci.raccicore.builders](../index.md)/[BannerBuilder](index.md)

# BannerBuilder

[jvm]\
class [BannerBuilder](index.md) : [BaseItemBuilder](../-base-item-builder/index.md)&lt;[BannerBuilder](index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [addFlag](../-base-item-builder/add-flag.md) | [jvm]<br>open fun [addFlag](../-base-item-builder/add-flag.md)(vararg flags: ItemFlag): [BannerBuilder](index.md) |
| [amount](../-base-item-builder/amount.md) | [jvm]<br>open fun [amount](../-base-item-builder/amount.md)(amount: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [BannerBuilder](index.md) |
| [baseColour](base-colour.md) | [jvm]<br>fun [baseColour](base-colour.md)(colour: DyeColor): [BannerBuilder](index.md) |
| [booleanNBT](../-base-item-builder/boolean-n-b-t.md) | [jvm]<br>open fun [booleanNBT](../-base-item-builder/boolean-n-b-t.md)(vararg pair: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt;): [BannerBuilder](index.md) |
| [build](build.md) | [jvm]<br>open override fun [build](build.md)(): ItemStack |
| [disenchant](../-base-item-builder/disenchant.md) | [jvm]<br>open fun [disenchant](../-base-item-builder/disenchant.md)(vararg enchants: Enchantment): [BannerBuilder](index.md) |
| [enchant](../-base-item-builder/enchant.md) | [jvm]<br>open fun [enchant](../-base-item-builder/enchant.md)(vararg enchants: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;Enchantment, [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt;): [BannerBuilder](index.md) |
| [glow](../-base-item-builder/glow.md) | [jvm]<br>open fun [glow](../-base-item-builder/glow.md)(): [BannerBuilder](index.md) |
| [lore](../-base-item-builder/lore.md) | [jvm]<br>~~open~~ ~~fun~~ [~~lore~~](../-base-item-builder/lore.md)~~(~~~~component~~~~:~~ [Function](https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;, [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;&gt;~~)~~~~:~~ [BannerBuilder](index.md)<br>open fun [lore](../-base-item-builder/lore.md)(vararg component: Component): [BannerBuilder](index.md)<br>open fun [lore](../-base-item-builder/lore.md)(unit: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;.() -&gt; [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;) |
| [model](../-base-item-builder/model.md) | [jvm]<br>open fun [model](../-base-item-builder/model.md)(modelData: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [BannerBuilder](index.md) |
| [name](../-base-item-builder/name.md) | [jvm]<br>open fun [name](../-base-item-builder/name.md)(component: Component): [BannerBuilder](index.md) |
| [pattern](pattern.md) | [jvm]<br>fun [pattern](pattern.md)(vararg pairs: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;DyeColor, PatternType&gt;): [BannerBuilder](index.md)<br>fun [pattern](pattern.md)(vararg pattern: Pattern): [BannerBuilder](index.md)<br>fun [pattern](pattern.md)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), color: DyeColor, pattern: PatternType): [BannerBuilder](index.md) |
| [pdc](../-base-item-builder/pdc.md) | [jvm]<br>open fun [pdc](../-base-item-builder/pdc.md)(unit: PersistentDataContainer.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [BannerBuilder](index.md) |
| [removeFlag](../-base-item-builder/remove-flag.md) | [jvm]<br>open fun [removeFlag](../-base-item-builder/remove-flag.md)(vararg flags: ItemFlag): [BannerBuilder](index.md) |
| [removeNBT](../-base-item-builder/remove-n-b-t.md) | [jvm]<br>open fun [removeNBT](../-base-item-builder/remove-n-b-t.md)(vararg key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [BannerBuilder](index.md) |
| [setPatterns](set-patterns.md) | [jvm]<br>fun [setPatterns](set-patterns.md)(patterns: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Pattern&gt;): [BannerBuilder](index.md) |
| [stringNBT](../-base-item-builder/string-n-b-t.md) | [jvm]<br>open fun [stringNBT](../-base-item-builder/string-n-b-t.md)(vararg pair: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;): [BannerBuilder](index.md) |
| [unbreakable](../-base-item-builder/unbreakable.md) | [jvm]<br>open fun [unbreakable](../-base-item-builder/unbreakable.md)(unbreakable: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true): [BannerBuilder](index.md) |

## Properties

| Name | Summary |
|---|---|
| [baseColour](base-colour.md) | [jvm]<br>var [baseColour](base-colour.md): DyeColor? |
| [itemStack](../-base-item-builder/item-stack.md) | [jvm]<br>var [itemStack](../-base-item-builder/item-stack.md): ItemStack |
| [lore](../-base-item-builder/lore.md) | [jvm]<br>var [lore](../-base-item-builder/lore.md): Component |
| [meta](../-base-item-builder/meta.md) | [jvm]<br>var [meta](../-base-item-builder/meta.md): ItemMeta |
| [name](../-base-item-builder/name.md) | [jvm]<br>var [name](../-base-item-builder/name.md): @NullableComponent? |
| [patterns](patterns.md) | [jvm]<br>var [patterns](patterns.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Pattern&gt; |
