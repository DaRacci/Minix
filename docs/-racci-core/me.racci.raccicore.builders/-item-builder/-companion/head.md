//[RacciCore](../../../../index.md)/[me.racci.raccicore.api.builders](../../index.md)/[ItemBuilder](../index.md)/[Companion](index.md)/[head](head.md)

# head

[jvm]\
fun [head](head.md)(itemStack: ItemStack = ItemStack(Material.PLAYER_HEAD)): [HeadBuilder](../../-head-builder/index.md)

Method for creating a [HeadBuilder](../../-head-builder/index.md) which includes player head specific methods

#### Return

A new [HeadBuilder](../../-head-builder/index.md)

#### Since

0.1.5

## Parameters

jvm

| | |
|---|---|
| itemStack | An existing player head ItemStack if none is supplied a new Material.PLAYER_HEAD is created |

## Throws

| | |
|---|---|
| [kotlin.UnsupportedOperationException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unsupported-operation-exception/index.html) | if the item is not a player head |

[jvm]\
fun [head](head.md)(itemStack: ItemStack = ItemStack(Material.PLAYER_HEAD), builder: [HeadBuilder](../../-head-builder/index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {}): ItemStack
