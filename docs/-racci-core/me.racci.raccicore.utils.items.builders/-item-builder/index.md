//[RacciCore](../../../index.md)/[me.racci.raccicore.api.utils.items.builders](../index.md)/[ItemBuilder](index.md)

# ItemBuilder

[jvm]\
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")

~~class~~ [~~ItemBuilder~~](index.md) ~~:~~ [~~BaseItemBuilder~~](../-base-item-builder/index.md)~~&lt;~~[ItemBuilder](index.md)~~&gt;~~

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [amount](../-base-item-builder/amount.md) | [jvm]<br>open fun [amount](../-base-item-builder/amount.md)(amount: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [ItemBuilder](index.md) |
| [build](../-base-item-builder/build.md) | [jvm]<br>open fun [build](../-base-item-builder/build.md)(): ItemStack |
| [disenchant](../-base-item-builder/disenchant.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [disenchant](../-base-item-builder/disenchant.md)(enchantment: Enchantment): [ItemBuilder](index.md) |
| [enchant](../-base-item-builder/enchant.md) | [jvm]<br>@Contract(value = "_, _, _ -&gt; this")<br>open fun [enchant](../-base-item-builder/enchant.md)(enchantment: Enchantment, level: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 1): [ItemBuilder](index.md) |
| [flags](../-base-item-builder/flags.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [flags](../-base-item-builder/flags.md)(vararg flags: ItemFlag): [ItemBuilder](index.md) |
| [glow](../-base-item-builder/glow.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [glow](../-base-item-builder/glow.md)(glow: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true): [ItemBuilder](index.md) |
| [lore](../-base-item-builder/lore.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [lore](../-base-item-builder/lore.md)(lore: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;&gt;): [ItemBuilder](index.md)<br>@Contract(value = "_ -&gt; this")<br>open fun [lore](../-base-item-builder/lore.md)(vararg lore: Component): [ItemBuilder](index.md)<br>@Contract(value = "_ -&gt; this")<br>open fun [lore](../-base-item-builder/lore.md)(lore: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;): [ItemBuilder](index.md) |
| [model](../-base-item-builder/model.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [model](../-base-item-builder/model.md)(modelData: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [ItemBuilder](index.md) |
| [name](../-base-item-builder/name.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [name](../-base-item-builder/name.md)(name: Component): [ItemBuilder](index.md) |
| [pdc](../-base-item-builder/pdc.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [pdc](../-base-item-builder/pdc.md)(consumer: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;PersistentDataContainer&gt;): [ItemBuilder](index.md) |
| [removeNbt](../-base-item-builder/remove-nbt.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [removeNbt](../-base-item-builder/remove-nbt.md)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [ItemBuilder](index.md) |
| [setNbt](../-base-item-builder/set-nbt.md) | [jvm]<br>@Contract(value = "_, _ -&gt; this")<br>open fun [setNbt](../-base-item-builder/set-nbt.md)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [ItemBuilder](index.md)<br>@Contract(value = "_, _ -&gt; this")<br>open fun [setNbt](../-base-item-builder/set-nbt.md)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [ItemBuilder](index.md) |
| [unbreakable](../-base-item-builder/unbreakable.md) | [jvm]<br>@Contract(value = " -&gt; this")<br>open fun [unbreakable](../-base-item-builder/unbreakable.md)(): [ItemBuilder](index.md)<br>@Contract(value = "_ -&gt; this")<br>open fun [unbreakable](../-base-item-builder/unbreakable.md)(unbreakable: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [ItemBuilder](index.md) |

## Properties

| Name | Summary |
|---|---|
| [itemStack](../-base-item-builder/item-stack.md) | [jvm]<br>var [itemStack](../-base-item-builder/item-stack.md): ItemStack |
| [meta](../-base-item-builder/meta.md) | [jvm]<br>var [meta](../-base-item-builder/meta.md): ItemMeta |
