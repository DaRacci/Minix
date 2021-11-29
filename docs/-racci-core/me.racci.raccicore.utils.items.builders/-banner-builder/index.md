//[RacciCore](../../../index.md)/[me.racci.raccicore.api.utils.items.builders](../index.md)/[BannerBuilder](index.md)

# BannerBuilder

[jvm]\
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")

~~class~~ [~~BannerBuilder~~](index.md) ~~:~~ [~~BaseItemBuilder~~](../-base-item-builder/index.md)~~&lt;~~[BannerBuilder](index.md)~~&gt;~~

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [amount](../-base-item-builder/amount.md) | [jvm]<br>open fun [amount](../-base-item-builder/amount.md)(amount: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [BannerBuilder](index.md) |
| [baseColor](base-color.md) | [jvm]<br>fun [baseColor](base-color.md)(color: DyeColor): [BannerBuilder](index.md)<br>Sets the base color for this banner |
| [build](../-base-item-builder/build.md) | [jvm]<br>open fun [build](../-base-item-builder/build.md)(): ItemStack |
| [disenchant](../-base-item-builder/disenchant.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [disenchant](../-base-item-builder/disenchant.md)(enchantment: Enchantment): [BannerBuilder](index.md) |
| [enchant](../-base-item-builder/enchant.md) | [jvm]<br>@Contract(value = "_, _, _ -&gt; this")<br>open fun [enchant](../-base-item-builder/enchant.md)(enchantment: Enchantment, level: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 1): [BannerBuilder](index.md) |
| [flags](../-base-item-builder/flags.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [flags](../-base-item-builder/flags.md)(vararg flags: ItemFlag): [BannerBuilder](index.md) |
| [glow](../-base-item-builder/glow.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [glow](../-base-item-builder/glow.md)(glow: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true): [BannerBuilder](index.md) |
| [lore](../-base-item-builder/lore.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [lore](../-base-item-builder/lore.md)(lore: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;&gt;): [BannerBuilder](index.md)<br>@Contract(value = "_ -&gt; this")<br>open fun [lore](../-base-item-builder/lore.md)(vararg lore: Component): [BannerBuilder](index.md)<br>@Contract(value = "_ -&gt; this")<br>open fun [lore](../-base-item-builder/lore.md)(lore: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;): [BannerBuilder](index.md) |
| [model](../-base-item-builder/model.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [model](../-base-item-builder/model.md)(modelData: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [BannerBuilder](index.md) |
| [name](../-base-item-builder/name.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [name](../-base-item-builder/name.md)(name: Component): [BannerBuilder](index.md) |
| [pattern](pattern.md) | [jvm]<br>fun [pattern](pattern.md)(vararg pattern: Pattern?): [BannerBuilder](index.md)<br>Adds new patterns on top of the existing patterns<br>[jvm]<br>fun [pattern](pattern.md)(color: DyeColor, pattern: PatternType): [BannerBuilder](index.md)<br>Adds a new pattern on top of the existing patterns<br>[jvm]<br>fun [pattern](pattern.md)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), color: DyeColor, pattern: PatternType): [BannerBuilder](index.md)<br>Sets the pattern at the specified index |
| [pdc](../-base-item-builder/pdc.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [pdc](../-base-item-builder/pdc.md)(consumer: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;PersistentDataContainer&gt;): [BannerBuilder](index.md) |
| [removeNbt](../-base-item-builder/remove-nbt.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [removeNbt](../-base-item-builder/remove-nbt.md)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [BannerBuilder](index.md) |
| [setNbt](../-base-item-builder/set-nbt.md) | [jvm]<br>@Contract(value = "_, _ -&gt; this")<br>open fun [setNbt](../-base-item-builder/set-nbt.md)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [BannerBuilder](index.md)<br>@Contract(value = "_, _ -&gt; this")<br>open fun [setNbt](../-base-item-builder/set-nbt.md)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [BannerBuilder](index.md) |
| [setPatterns](set-patterns.md) | [jvm]<br>fun [setPatterns](set-patterns.md)(patterns: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Pattern?&gt;): [BannerBuilder](index.md)<br>Sets the patterns used on this banner |
| [unbreakable](../-base-item-builder/unbreakable.md) | [jvm]<br>@Contract(value = " -&gt; this")<br>open fun [unbreakable](../-base-item-builder/unbreakable.md)(): [BannerBuilder](index.md)<br>@Contract(value = "_ -&gt; this")<br>open fun [unbreakable](../-base-item-builder/unbreakable.md)(unbreakable: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [BannerBuilder](index.md) |

## Properties

| Name | Summary |
|---|---|
| [itemStack](../-base-item-builder/item-stack.md) | [jvm]<br>var [itemStack](../-base-item-builder/item-stack.md): ItemStack |
| [meta](../-base-item-builder/meta.md) | [jvm]<br>var [meta](../-base-item-builder/meta.md): ItemMeta |
