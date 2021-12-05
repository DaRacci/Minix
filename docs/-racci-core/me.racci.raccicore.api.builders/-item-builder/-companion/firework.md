---
title: firework
---
//[RacciCore](../../../../index.html)/[me.racci.raccicore.api.builders](../../index.html)/[ItemBuilder](../index.html)/[Companion](index.html)/[firework](firework.html)



# firework



[jvm]\
fun [firework](firework.html)(itemStack: ItemStack = ItemStack(Material.FIREWORK_ROCKET)): [FireworkBuilder](../../-firework-builder/index.html)



Method for creating a [FireworkBuilder](../../-firework-builder/index.html) which includes firework specific methods



#### Return



A new [FireworkBuilder](../../-firework-builder/index.html)



#### Since



0.1.5



## Parameters


jvm

| | |
|---|---|
| itemStack | an existing firework ItemStack if none is supplied a new Material.FIREWORK_ROCKET is created |



## Throws


| | |
|---|---|
| [kotlin.UnsupportedOperationException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unsupported-operation-exception/index.html) | if the item type is not a firework |




[jvm]\
fun [firework](firework.html)(itemStack: ItemStack = ItemStack(Material.FIREWORK_ROCKET), builder: [FireworkBuilder](../../-firework-builder/index.html).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {}): ItemStack




