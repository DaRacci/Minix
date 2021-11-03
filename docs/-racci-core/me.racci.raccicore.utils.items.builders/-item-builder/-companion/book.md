//[RacciCore](../../../../index.md)/[me.racci.raccicore.utils.items.builders](../../index.md)/[ItemBuilder](../index.md)/[Companion](index.md)/[book](book.md)

# book

[jvm]\
fun [book](book.md)(itemStack: ItemStack): [BookBuilder](../../-book-builder/index.md)

Method for creating a [BookBuilder](../../-book-builder/index.md) which will have Material.WRITABLE_BOOK / Material.WRITTEN_BOOK specific methods

#### Return

A new [FireworkBuilder](../../-firework-builder/index.md)

#### Since

3.0.1

## Parameters

jvm

| | |
|---|---|
| itemStack | an existing Material.WRITABLE_BOOK / Material.WRITTEN_BOOK |

## Throws

| | |
|---|---|
| [kotlin.UnsupportedOperationException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unsupported-operation-exception/index.html) | if the item type is not Material.WRITABLE_BOOK or Material.WRITTEN_BOOK |
