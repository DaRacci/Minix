---
title: from
---
//[RacciCore](../../../../index.html)/[me.racci.raccicore.api.builders](../../index.html)/[ItemBuilder](../index.html)/[Companion](index.html)/[from](from.html)



# from



[jvm]\
fun [from](from.html)(itemStack: ItemStack): [ItemBuilder](../index.html)



Method for creating a new [ItemBuilder](../index.html)



#### Return



A new [ItemBuilder](../index.html)



#### Since



0.1.5



## Parameters


jvm

| | |
|---|---|
| itemStack | an existing ItemStack |





[jvm]\
fun [from](from.html)(itemStack: ItemStack, builder: [ItemBuilder](../index.html).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {}): ItemStack

fun [from](from.html)(material: Material, builder: [ItemBuilder](../index.html).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {}): ItemStack





[jvm]\
fun [from](from.html)(material: Material): [ItemBuilder](../index.html)



Method for creating a new [ItemBuilder](../index.html)



#### Return



A new [ItemBuilder](../index.html)



#### Since



0.1.5



## Parameters


jvm

| | |
|---|---|
| material | the Material of the new ItemStack |




