//[RacciCore](../../../../index.md)/[me.racci.raccicore.builders](../../index.md)/[ItemBuilder](../index.md)/[Companion](index.md)/[star](star.md)

# star

[jvm]\
fun [star](star.md)(itemStack: ItemStack = ItemStack(Material.FIREWORK_STAR)): [FireworkBuilder](../../-firework-builder/index.md)

Method for creating a [FireworkBuilder](../../-firework-builder/index.md) which includes firework star specific methods

#### Return

A new [FireworkBuilder](../../-firework-builder/index.md)

#### Since

0.1.5

## Parameters

jvm

| | |
|---|---|
| itemStack | an existing firework star ItemStack if none is supplied a new Material.FIREWORK_STAR is created |

## Throws

| | |
|---|---|
| [kotlin.UnsupportedOperationException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unsupported-operation-exception/index.html) | if the item type is not a firework star |

[jvm]\
fun [star](star.md)(itemStack: ItemStack = ItemStack(Material.FIREWORK_STAR), builder: [FireworkBuilder](../../-firework-builder/index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {}): ItemStack
