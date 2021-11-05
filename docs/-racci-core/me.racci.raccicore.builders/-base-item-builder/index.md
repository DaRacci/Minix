//[RacciCore](../../../index.md)/[me.racci.raccicore.builders](../index.md)/[BaseItemBuilder](index.md)

# BaseItemBuilder

[jvm]\
abstract class [BaseItemBuilder](index.md)&lt;[T](index.md) : [BaseItemBuilder](index.md)&lt;[T](index.md)&gt;&gt;

## Functions

| Name | Summary |
|---|---|
| [addFlag](add-flag.md) | [jvm]<br>open fun [addFlag](add-flag.md)(vararg flags: ItemFlag): [T](index.md) |
| [amount](amount.md) | [jvm]<br>open fun [amount](amount.md)(amount: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [T](index.md) |
| [booleanNBT](boolean-n-b-t.md) | [jvm]<br>open fun [booleanNBT](boolean-n-b-t.md)(vararg pair: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt;): [T](index.md) |
| [build](build.md) | [jvm]<br>open fun [build](build.md)(): ItemStack |
| [disenchant](disenchant.md) | [jvm]<br>open fun [disenchant](disenchant.md)(vararg enchants: Enchantment): [T](index.md) |
| [enchant](enchant.md) | [jvm]<br>open fun [enchant](enchant.md)(vararg enchants: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;Enchantment, [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt;): [T](index.md) |
| [glow](glow.md) | [jvm]<br>open fun [glow](glow.md)(boolean: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true): [T](index.md) |
| [lore](lore.md) | [jvm]<br>~~open~~ ~~fun~~ [~~lore~~](lore.md)~~(~~~~component~~~~:~~ [Function](https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;, [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;&gt;~~)~~~~:~~ [~~T~~](index.md)<br>open fun [lore](lore.md)(vararg component: Component): [T](index.md)<br>open fun [lore](lore.md)(unit: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;.() -&gt; [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;) |
| [model](model.md) | [jvm]<br>open fun [model](model.md)(modelData: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [T](index.md) |
| [name](name.md) | [jvm]<br>open fun [name](name.md)(component: Component): [T](index.md) |
| [pdc](pdc.md) | [jvm]<br>open fun [pdc](pdc.md)(unit: PersistentDataContainer.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [T](index.md) |
| [removeFlag](remove-flag.md) | [jvm]<br>open fun [removeFlag](remove-flag.md)(vararg flags: ItemFlag): [T](index.md) |
| [removeNBT](remove-n-b-t.md) | [jvm]<br>open fun [removeNBT](remove-n-b-t.md)(vararg key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [T](index.md) |
| [stringNBT](string-n-b-t.md) | [jvm]<br>open fun [stringNBT](string-n-b-t.md)(vararg pair: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;): [T](index.md) |
| [unbreakable](unbreakable.md) | [jvm]<br>open fun [unbreakable](unbreakable.md)(unbreakable: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true): [T](index.md) |

## Properties

| Name | Summary |
|---|---|
| [amount](amount.md) | [jvm]<br>var [amount](amount.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [glow](glow.md) | [jvm]<br>var [glow](glow.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [itemStack](item-stack.md) | [jvm]<br>var [itemStack](item-stack.md): ItemStack |
| [lore](lore.md) | [jvm]<br>var [lore](lore.md): Component |
| [meta](meta.md) | [jvm]<br>var [meta](meta.md): ItemMeta |
| [name](name.md) | [jvm]<br>var [name](name.md): @NullableComponent? |
| [nbt](nbt.md) | [jvm]<br>var [nbt](nbt.md): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt; |
| [unbreakable](unbreakable.md) | [jvm]<br>var [unbreakable](unbreakable.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

## Inheritors

| Name |
|---|
| [BannerBuilder](../-banner-builder/index.md) |
| [BookBuilder](../-book-builder/index.md) |
| [FireworkBuilder](../-firework-builder/index.md) |
| [HeadBuilder](../-head-builder/index.md) |
| [ItemBuilder](../-item-builder/index.md) |
| [MapBuilder](../-map-builder/index.md) |
