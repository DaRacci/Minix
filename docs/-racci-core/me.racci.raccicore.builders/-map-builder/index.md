//[RacciCore](../../../index.md)/[me.racci.raccicore.builders](../index.md)/[MapBuilder](index.md)

# MapBuilder

[jvm]\
class [MapBuilder](index.md) : [BaseItemBuilder](../-base-item-builder/index.md)&lt;[MapBuilder](index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [addFlag](../-base-item-builder/add-flag.md) | [jvm]<br>open fun [addFlag](../-base-item-builder/add-flag.md)(vararg flags: ItemFlag): [MapBuilder](index.md) |
| [booleanNBT](../-base-item-builder/boolean-n-b-t.md) | [jvm]<br>open fun [booleanNBT](../-base-item-builder/boolean-n-b-t.md)(vararg pair: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt;): [MapBuilder](index.md) |
| [build](build.md) | [jvm]<br>open override fun [build](build.md)(): ItemStack |
| [disenchant](../-base-item-builder/disenchant.md) | [jvm]<br>open fun [disenchant](../-base-item-builder/disenchant.md)(vararg enchants: Enchantment): [MapBuilder](index.md) |
| [enchant](../-base-item-builder/enchant.md) | [jvm]<br>open fun [enchant](../-base-item-builder/enchant.md)(vararg enchants: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;Enchantment, [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt;): [MapBuilder](index.md) |
| [glow](../-base-item-builder/glow.md) | [jvm]<br>open fun [glow](../-base-item-builder/glow.md)(boolean: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true): [MapBuilder](index.md) |
| [lore](../-base-item-builder/lore.md) | [jvm]<br>open fun [lore](../-base-item-builder/lore.md)(vararg component: Component): [MapBuilder](index.md)<br>open fun [lore](../-base-item-builder/lore.md)(unit: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;.() -&gt; [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Component&gt;) |
| [pdc](../-base-item-builder/pdc.md) | [jvm]<br>open fun [pdc](../-base-item-builder/pdc.md)(unit: PersistentDataContainer.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [MapBuilder](index.md) |
| [removeFlag](../-base-item-builder/remove-flag.md) | [jvm]<br>open fun [removeFlag](../-base-item-builder/remove-flag.md)(vararg flags: ItemFlag): [MapBuilder](index.md) |
| [removeNBT](../-base-item-builder/remove-n-b-t.md) | [jvm]<br>open fun [removeNBT](../-base-item-builder/remove-n-b-t.md)(vararg key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [MapBuilder](index.md) |
| [stringNBT](../-base-item-builder/string-n-b-t.md) | [jvm]<br>open fun [stringNBT](../-base-item-builder/string-n-b-t.md)(vararg pair: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;): [MapBuilder](index.md) |
| [unbreakable](../-base-item-builder/unbreakable.md) | [jvm]<br>open fun [unbreakable](../-base-item-builder/unbreakable.md)(unbreakable: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true): [MapBuilder](index.md) |

## Properties

| Name | Summary |
|---|---|
| [amount](../-base-item-builder/amount.md) | [jvm]<br>var [amount](../-base-item-builder/amount.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [colour](colour.md) | [jvm]<br>var [colour](colour.md): Color?<br>Gets or Sets the map colour. A custom map colour will alter the display of the map in an inventory slot. |
| [glow](../-base-item-builder/glow.md) | [jvm]<br>var [glow](../-base-item-builder/glow.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [itemStack](../-base-item-builder/item-stack.md) | [jvm]<br>var [itemStack](../-base-item-builder/item-stack.md): ItemStack |
| [locName](loc-name.md) | [jvm]<br>var [locName](loc-name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?<br>Gets or Sets the location name. A custom map colour will alter the display of the map in an inventory slot. |
| [lore](../-base-item-builder/lore.md) | [jvm]<br>var [lore](../-base-item-builder/lore.md): Component |
| [meta](../-base-item-builder/meta.md) | [jvm]<br>var [meta](../-base-item-builder/meta.md): ItemMeta |
| [model](../-base-item-builder/model.md) | [jvm]<br>var [model](../-base-item-builder/model.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [name](../-base-item-builder/name.md) | [jvm]<br>var [name](../-base-item-builder/name.md): @NullableComponent? |
| [nbt](../-base-item-builder/nbt.md) | [jvm]<br>var [nbt](../-base-item-builder/nbt.md): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt; |
| [pdc](../-base-item-builder/pdc.md) | [jvm]<br>val [pdc](../-base-item-builder/pdc.md): @NotNullPersistentDataContainer |
| [scaling](scaling.md) | [jvm]<br>var [scaling](scaling.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Gets or Sets if this map is scaling or not. |
| [unbreakable](../-base-item-builder/unbreakable.md) | [jvm]<br>var [unbreakable](../-base-item-builder/unbreakable.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [view](view.md) | [jvm]<br>var [view](view.md): MapView?<br>Gets or Sets the associated map. This is used to determine what map is displayed. |
