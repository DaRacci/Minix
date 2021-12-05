---
title: star
---
//[RacciCore](../../../../index.html)/[me.racci.raccicore.api.builders](../../index.html)/[ItemBuilder](../index.html)/[Companion](index.html)/[star](star.html)



# star



[jvm]\
fun [star](star.html)(itemStack: ItemStack = ItemStack(Material.FIREWORK_STAR)): [FireworkBuilder](../../-firework-builder/index.html)



Method for creating a [FireworkBuilder](../../-firework-builder/index.html) which includes firework star specific methods



#### Return



A new [FireworkBuilder](../../-firework-builder/index.html)



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
fun [star](star.html)(itemStack: ItemStack = ItemStack(Material.FIREWORK_STAR), builder: [FireworkBuilder](../../-firework-builder/index.html).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {}): ItemStack




