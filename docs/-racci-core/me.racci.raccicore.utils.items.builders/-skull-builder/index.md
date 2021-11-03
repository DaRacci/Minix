//[RacciCore](../../../index.md)/[me.racci.raccicore.utils.items.builders](../index.md)/[SkullBuilder](index.md)

# SkullBuilder

[jvm]\
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")

~~class~~ [~~SkullBuilder~~](index.md) ~~:~~ [~~BaseItemBuilder~~](../-base-item-builder/index.md)~~&lt;~~[SkullBuilder](index.md)~~&gt;~~

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [amount](../-base-item-builder/amount.md) | [jvm]<br>open fun [amount](../-base-item-builder/amount.md)(amount: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [SkullBuilder](index.md) |
| [build](../-base-item-builder/build.md) | [jvm]<br>open fun [build](../-base-item-builder/build.md)(): ItemStack |
| [disenchant](../-base-item-builder/disenchant.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [disenchant](../-base-item-builder/disenchant.md)(enchantment: Enchantment): [SkullBuilder](index.md) |
| [enchant](../-base-item-builder/enchant.md) | [jvm]<br>@Contract(value = "_, _, _ -&gt; this")<br>open fun [enchant](../-base-item-builder/enchant.md)(enchantment: Enchantment, level: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 1): [SkullBuilder](index.md) |
| [flags](../-base-item-builder/flags.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [flags](../-base-item-builder/flags.md)(vararg flags: ItemFlag): [SkullBuilder](index.md) |
| [glow](../-base-item-builder/glow.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [glow](../-base-item-builder/glow.md)(glow: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true): [SkullBuilder](index.md) |
| [lore](../-base-item-builder/lore.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [lore](../-base-item-builder/lore.md)(lore: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;&gt;): [SkullBuilder](index.md)<br>@Contract(value = "_ -&gt; this")<br>open fun [lore](../-base-item-builder/lore.md)(vararg lore: Component): [SkullBuilder](index.md)<br>@Contract(value = "_ -&gt; this")<br>open fun [lore](../-base-item-builder/lore.md)(lore: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;): [SkullBuilder](index.md) |
| [model](../-base-item-builder/model.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [model](../-base-item-builder/model.md)(modelData: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [SkullBuilder](index.md) |
| [name](../-base-item-builder/name.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [name](../-base-item-builder/name.md)(name: Component): [SkullBuilder](index.md) |
| [owner](owner.md) | [jvm]<br>fun [owner](owner.md)(player: OfflinePlayer): [SkullBuilder](index.md)<br>Sets skull owner via bukkit methods |
| [pdc](../-base-item-builder/pdc.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [pdc](../-base-item-builder/pdc.md)(consumer: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;PersistentDataContainer&gt;): [SkullBuilder](index.md) |
| [removeNbt](../-base-item-builder/remove-nbt.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [removeNbt](../-base-item-builder/remove-nbt.md)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [SkullBuilder](index.md) |
| [setNbt](../-base-item-builder/set-nbt.md) | [jvm]<br>@Contract(value = "_, _ -&gt; this")<br>open fun [setNbt](../-base-item-builder/set-nbt.md)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [SkullBuilder](index.md)<br>@Contract(value = "_, _ -&gt; this")<br>open fun [setNbt](../-base-item-builder/set-nbt.md)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [SkullBuilder](index.md) |
| [texture](texture.md) | [jvm]<br>fun [texture](texture.md)(texture: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [SkullBuilder](index.md)<br>Sets the skull texture using a BASE64 string |
| [unbreakable](../-base-item-builder/unbreakable.md) | [jvm]<br>@Contract(value = " -&gt; this")<br>open fun [unbreakable](../-base-item-builder/unbreakable.md)(): [SkullBuilder](index.md)<br>@Contract(value = "_ -&gt; this")<br>open fun [unbreakable](../-base-item-builder/unbreakable.md)(unbreakable: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [SkullBuilder](index.md) |

## Properties

| Name | Summary |
|---|---|
| [itemStack](../-base-item-builder/item-stack.md) | [jvm]<br>var [itemStack](../-base-item-builder/item-stack.md): ItemStack |
| [meta](../-base-item-builder/meta.md) | [jvm]<br>var [meta](../-base-item-builder/meta.md): ItemMeta |
