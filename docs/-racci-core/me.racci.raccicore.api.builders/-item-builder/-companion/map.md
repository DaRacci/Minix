---
title: map
---
//[RacciCore](../../../../index.html)/[me.racci.raccicore.api.builders](../../index.html)/[ItemBuilder](../index.html)/[Companion](index.html)/[map](map.html)



# map



[jvm]\
fun [map](map.html)(itemStack: ItemStack = ItemStack(Material.MAP)): [MapBuilder](../../-map-builder/index.html)



Method for creating a [MapBuilder](../../-map-builder/index.html) which includes map specific methods



#### Return



A new [MapBuilder](../../-map-builder/index.html)



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
fun [map](map.html)(itemStack: ItemStack = ItemStack(Material.MAP), builder: [MapBuilder](../../-map-builder/index.html).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {}): ItemStack




