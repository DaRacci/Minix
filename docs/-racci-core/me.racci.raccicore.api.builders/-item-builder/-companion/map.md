//[RacciCore](../../../../index.md)/[me.racci.raccicore.api.builders](../../index.md)/[ItemBuilder](../index.md)/[Companion](index.md)/[map](map.md)

# map

[jvm]\
fun [map](map.md)(itemStack: ItemStack = ItemStack(Material.MAP)): [MapBuilder](../../-map-builder/index.md)

Method for creating a [MapBuilder](../../-map-builder/index.md) which includes map specific methods

#### Return

A new [MapBuilder](../../-map-builder/index.md)

#### Since

0.1.5

## Parameters

jvm

| | |
|---|---|
| itemStack | An existing map ItemStack if none is supplied a new Material.MAP is created |

## Throws

| | |
|---|---|
| [kotlin.UnsupportedOperationException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unsupported-operation-exception/index.html) | if the item type is not a map |

[jvm]\
fun [map](map.md)(itemStack: ItemStack = ItemStack(Material.MAP), builder: [MapBuilder](../../-map-builder/index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {}): ItemStack
