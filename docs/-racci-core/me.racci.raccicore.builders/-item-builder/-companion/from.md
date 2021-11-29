//[RacciCore](../../../../index.md)/[me.racci.raccicore.api.builders](../../index.md)/[ItemBuilder](../index.md)/[Companion](index.md)/[from](from.md)

# from

[jvm]\
fun [from](from.md)(itemStack: ItemStack): [ItemBuilder](../index.md)

Method for creating a new [ItemBuilder](../index.md)

#### Return

A new [ItemBuilder](../index.md)

#### Since

0.1.5

## Parameters

jvm

| | |
|---|---|
| itemStack | an existing ItemStack |

[jvm]\
fun [from](from.md)(itemStack: ItemStack, builder: [ItemBuilder](../index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {}): ItemStack

fun [from](from.md)(material: Material, builder: [ItemBuilder](../index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {}): ItemStack

[jvm]\
fun [from](from.md)(material: Material): [ItemBuilder](../index.md)

Method for creating a new [ItemBuilder](../index.md)

#### Return

A new [ItemBuilder](../index.md)

#### Since

0.1.5

## Parameters

jvm

| | |
|---|---|
| material | the Material of the new ItemStack |
