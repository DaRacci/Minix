//[RacciCore](../../../../index.md)/[me.racci.raccicore.api.utils.items.builders](../../index.md)/[ItemBuilder](../index.md)/[Companion](index.md)/[skull](skull.md)

# skull

[jvm]\
fun [skull](skull.md)(): [SkullBuilder](../../-skull-builder/index.md)

Method for creating a [SkullBuilder](../../-skull-builder/index.md) which will have PLAYER_HEAD specific methods

#### Return

A new [SkullBuilder](../../-skull-builder/index.md)

[jvm]\
fun [skull](skull.md)(itemStack: ItemStack): [SkullBuilder](../../-skull-builder/index.md)

Method for creating a [SkullBuilder](../../-skull-builder/index.md) which will have PLAYER_HEAD specific methods

#### Return

A new [SkullBuilder](../../-skull-builder/index.md)

## Parameters

jvm

| | |
|---|---|
| itemStack | An existing PLAYER_HEAD ItemStack |

## Throws

| | |
|---|---|
| [kotlin.UnsupportedOperationException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unsupported-operation-exception/index.html) | if the item is not a player head |
