---
title: banner
---
//[RacciCore](../../../../index.html)/[me.racci.raccicore.api.builders](../../index.html)/[ItemBuilder](../index.html)/[Companion](index.html)/[banner](banner.html)



# banner



[jvm]\
fun [banner](banner.html)(itemStack: ItemStack = ItemStack(Material.WHITE_BANNER)): [BannerBuilder](../../-banner-builder/index.html)



Method for creating a new [BannerBuilder](../../-banner-builder/index.html) which includes banner specific methods



#### Return



A new [BannerBuilder](../../-banner-builder/index.html)



#### Since



0.1.5



## Parameters


jvm

| | |
|---|---|
| itemStack | an existing banner ItemStack if none is supplied a new Material.WHITE_BANNER is created |



## Throws


| | |
|---|---|
| [kotlin.UnsupportedOperationException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unsupported-operation-exception/index.html) | if the item is not a banner |




[jvm]\
fun [banner](banner.html)(itemStack: ItemStack = ItemStack(Material.WHITE_BANNER), builder: [BannerBuilder](../../-banner-builder/index.html).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {}): ItemStack




