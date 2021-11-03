//[RacciCore](../../../index.md)/[me.racci.raccicore.utils.items.builders](../index.md)/[MapBuilder](index.md)

# MapBuilder

[jvm]\
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")

~~class~~ [~~MapBuilder~~](index.md) ~~:~~ [~~BaseItemBuilder~~](../-base-item-builder/index.md)~~&lt;~~[MapBuilder](index.md)~~&gt;~~

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [amount](../-base-item-builder/amount.md) | [jvm]<br>open fun [amount](../-base-item-builder/amount.md)(amount: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [MapBuilder](index.md) |
| [build](../-base-item-builder/build.md) | [jvm]<br>open fun [build](../-base-item-builder/build.md)(): ItemStack |
| [color](color.md) | [jvm]<br>fun [color](color.md)(color: Color?): [MapBuilder](index.md)<br>Sets the map color. A custom map color will alter the display of the map in an inventory slot. |
| [disenchant](../-base-item-builder/disenchant.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [disenchant](../-base-item-builder/disenchant.md)(enchantment: Enchantment): [MapBuilder](index.md) |
| [enchant](../-base-item-builder/enchant.md) | [jvm]<br>@Contract(value = "_, _, _ -&gt; this")<br>open fun [enchant](../-base-item-builder/enchant.md)(enchantment: Enchantment, level: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 1): [MapBuilder](index.md) |
| [flags](../-base-item-builder/flags.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [flags](../-base-item-builder/flags.md)(vararg flags: ItemFlag): [MapBuilder](index.md) |
| [glow](../-base-item-builder/glow.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [glow](../-base-item-builder/glow.md)(glow: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true): [MapBuilder](index.md) |
| [locationName](location-name.md) | [jvm]<br>fun [locationName](location-name.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?): [MapBuilder](index.md)<br>Sets the location name. A custom map color will alter the display of the map in an inventory slot. |
| [lore](../-base-item-builder/lore.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [lore](../-base-item-builder/lore.md)(lore: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;&gt;): [MapBuilder](index.md)<br>@Contract(value = "_ -&gt; this")<br>open fun [lore](../-base-item-builder/lore.md)(vararg lore: Component): [MapBuilder](index.md)<br>@Contract(value = "_ -&gt; this")<br>open fun [lore](../-base-item-builder/lore.md)(lore: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;): [MapBuilder](index.md) |
| [model](../-base-item-builder/model.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [model](../-base-item-builder/model.md)(modelData: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [MapBuilder](index.md) |
| [name](../-base-item-builder/name.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [name](../-base-item-builder/name.md)(name: Component): [MapBuilder](index.md) |
| [pdc](../-base-item-builder/pdc.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [pdc](../-base-item-builder/pdc.md)(consumer: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;PersistentDataContainer&gt;): [MapBuilder](index.md) |
| [removeNbt](../-base-item-builder/remove-nbt.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [removeNbt](../-base-item-builder/remove-nbt.md)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [MapBuilder](index.md) |
| [scaling](scaling.md) | [jvm]<br>fun [scaling](scaling.md)(scaling: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [MapBuilder](index.md)<br>Sets if this map is scaling or not. |
| [setNbt](../-base-item-builder/set-nbt.md) | [jvm]<br>@Contract(value = "_, _ -&gt; this")<br>open fun [setNbt](../-base-item-builder/set-nbt.md)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [MapBuilder](index.md)<br>@Contract(value = "_, _ -&gt; this")<br>open fun [setNbt](../-base-item-builder/set-nbt.md)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [MapBuilder](index.md) |
| [unbreakable](../-base-item-builder/unbreakable.md) | [jvm]<br>@Contract(value = " -&gt; this")<br>open fun [unbreakable](../-base-item-builder/unbreakable.md)(): [MapBuilder](index.md)<br>@Contract(value = "_ -&gt; this")<br>open fun [unbreakable](../-base-item-builder/unbreakable.md)(unbreakable: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [MapBuilder](index.md) |
| [view](view.md) | [jvm]<br>fun [view](view.md)(view: MapView): [MapBuilder](index.md)<br>Sets the associated map. This is used to determine what map is displayed. |

## Properties

| Name | Summary |
|---|---|
| [itemStack](../-base-item-builder/item-stack.md) | [jvm]<br>var [itemStack](../-base-item-builder/item-stack.md): ItemStack |
| [meta](../-base-item-builder/meta.md) | [jvm]<br>var [meta](../-base-item-builder/meta.md): ItemMeta |
