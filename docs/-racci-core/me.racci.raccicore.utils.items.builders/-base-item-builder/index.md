//[RacciCore](../../../index.md)/[me.racci.raccicore.utils.items.builders](../index.md)/[BaseItemBuilder](index.md)

# BaseItemBuilder

[jvm]\
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")

~~abstract~~ ~~class~~ [~~BaseItemBuilder~~](index.md)~~&lt;~~[B](index.md) : [BaseItemBuilder](index.md)&lt;[B](index.md)&gt;~~&gt;~~

## Functions

| Name | Summary |
|---|---|
| [amount](amount.md) | [jvm]<br>open fun [amount](amount.md)(amount: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [B](index.md) |
| [build](build.md) | [jvm]<br>open fun [build](build.md)(): ItemStack |
| [disenchant](disenchant.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [disenchant](disenchant.md)(enchantment: Enchantment): [B](index.md) |
| [enchant](enchant.md) | [jvm]<br>@Contract(value = "_, _, _ -&gt; this")<br>open fun [enchant](enchant.md)(enchantment: Enchantment, level: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 1): [B](index.md) |
| [flags](flags.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [flags](flags.md)(vararg flags: ItemFlag): [B](index.md) |
| [glow](glow.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [glow](glow.md)(glow: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true): [B](index.md) |
| [lore](lore.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [lore](lore.md)(lore: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;&gt;): [B](index.md)<br>@Contract(value = "_ -&gt; this")<br>open fun [lore](lore.md)(vararg lore: Component): [B](index.md)<br>@Contract(value = "_ -&gt; this")<br>open fun [lore](lore.md)(lore: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;): [B](index.md) |
| [model](model.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [model](model.md)(modelData: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [B](index.md) |
| [name](name.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [name](name.md)(name: Component): [B](index.md) |
| [pdc](pdc.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [pdc](pdc.md)(consumer: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;PersistentDataContainer&gt;): [B](index.md) |
| [removeNbt](remove-nbt.md) | [jvm]<br>@Contract(value = "_ -&gt; this")<br>open fun [removeNbt](remove-nbt.md)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [B](index.md) |
| [setNbt](set-nbt.md) | [jvm]<br>@Contract(value = "_, _ -&gt; this")<br>open fun [setNbt](set-nbt.md)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [B](index.md)<br>@Contract(value = "_, _ -&gt; this")<br>open fun [setNbt](set-nbt.md)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [B](index.md) |
| [unbreakable](unbreakable.md) | [jvm]<br>@Contract(value = " -&gt; this")<br>open fun [unbreakable](unbreakable.md)(): [B](index.md)<br>@Contract(value = "_ -&gt; this")<br>open fun [unbreakable](unbreakable.md)(unbreakable: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [B](index.md) |

## Properties

| Name | Summary |
|---|---|
| [itemStack](item-stack.md) | [jvm]<br>var [itemStack](item-stack.md): ItemStack |
| [meta](meta.md) | [jvm]<br>var [meta](meta.md): ItemMeta |

## Inheritors

| Name |
|---|
| [BannerBuilder](../-banner-builder/index.md) |
| [BookBuilder](../-book-builder/index.md) |
| [FireworkBuilder](../-firework-builder/index.md) |
| [ItemBuilder](../-item-builder/index.md) |
| [MapBuilder](../-map-builder/index.md) |
| [SkullBuilder](../-skull-builder/index.md) |
