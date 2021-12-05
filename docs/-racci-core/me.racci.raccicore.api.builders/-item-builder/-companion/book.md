---
title: book
---
//[RacciCore](../../../../index.html)/[me.racci.raccicore.api.builders](../../index.html)/[ItemBuilder](../index.html)/[Companion](index.html)/[book](book.html)



# book



[jvm]\
fun [book](book.html)(itemStack: ItemStack = ItemStack(Material.WRITTEN_BOOK)): [BookBuilder](../../-book-builder/index.html)



Method for creating a [BookBuilder](../../-book-builder/index.html) which includes book specific methods



#### Return



A new [BookBuilder](../../-book-builder/index.html)



#### Since



0.1.5



## Parameters


jvm

| | |
|---|---|
| itemStack | an existing book ItemStack, if none is supplied a new Material.WRITTEN_BOOK is created |



## Throws


| | |
|---|---|
| [kotlin.UnsupportedOperationException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unsupported-operation-exception/index.html) | if the item type is not a book |




[jvm]\
fun [book](book.html)(itemStack: ItemStack = ItemStack(Material.WRITTEN_BOOK), builder: [BookBuilder](../../-book-builder/index.html).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {}): ItemStack




