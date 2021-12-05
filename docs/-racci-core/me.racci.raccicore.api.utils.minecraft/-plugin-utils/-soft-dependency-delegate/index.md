---
title: SoftDependencyDelegate
---
//[RacciCore](../../../../index.html)/[me.racci.raccicore.api.utils.minecraft](../../index.html)/[PluginUtils](../index.html)/[SoftDependencyDelegate](index.html)



# SoftDependencyDelegate



[jvm]\
class [SoftDependencyDelegate](index.html)&lt;[T](index.html) : Plugin&gt;(pluginName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), type: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](index.html)&gt;) : [ReadOnlyProperty](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.properties/-read-only-property/index.html)&lt;[RacciPlugin](../../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), [T](index.html)?&gt;



## Functions


| Name | Summary |
|---|---|
| [getValue](get-value.html) | [jvm]<br>open operator override fun [getValue](get-value.html)(thisRef: [RacciPlugin](../../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), property: [KProperty](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property/index.html)&lt;*&gt;): [T](index.html)? |


## Properties


| Name | Summary |
|---|---|
| [pluginName](plugin-name.html) | [jvm]<br>val [pluginName](plugin-name.html): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [type](type.html) | [jvm]<br>val [type](type.html): [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](index.html)&gt; |

