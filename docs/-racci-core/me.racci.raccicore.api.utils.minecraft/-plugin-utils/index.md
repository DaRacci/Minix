---
title: PluginUtils
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.minecraft](../index.html)/[PluginUtils](index.html)



# PluginUtils



[jvm]\
object [PluginUtils](index.html)



## Types


| Name | Summary |
|---|---|
| [DependencyDelegate](-dependency-delegate/index.html) | [jvm]<br>class [DependencyDelegate](-dependency-delegate/index.html)&lt;[T](-dependency-delegate/index.html) : Plugin&gt;(pluginName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), type: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](-dependency-delegate/index.html)&gt;) : [ReadOnlyProperty](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.properties/-read-only-property/index.html)&lt;[RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), [T](-dependency-delegate/index.html)&gt; |
| [SoftDependencyDelegate](-soft-dependency-delegate/index.html) | [jvm]<br>class [SoftDependencyDelegate](-soft-dependency-delegate/index.html)&lt;[T](-soft-dependency-delegate/index.html) : Plugin&gt;(pluginName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), type: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](-soft-dependency-delegate/index.html)&gt;) : [ReadOnlyProperty](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.properties/-read-only-property/index.html)&lt;[RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), [T](-soft-dependency-delegate/index.html)?&gt; |


## Functions


| Name | Summary |
|---|---|
| [depend](depend.html) | [jvm]<br>inline fun &lt;[T](depend.html) : Plugin&gt; [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html).[depend](depend.html)(pluginName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PluginUtils.DependencyDelegate](-dependency-delegate/index.html)&lt;[T](depend.html)&gt;<br>Delegate that returns the plugin dependency, disable the plugin if the plugin is not available.<br>[jvm]<br>fun &lt;[T](depend.html) : Plugin&gt; [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html).[depend](depend.html)(type: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](depend.html)&gt;, pluginName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PluginUtils.DependencyDelegate](-dependency-delegate/index.html)&lt;[T](depend.html)&gt; |
| [softDepend](soft-depend.html) | [jvm]<br>inline fun &lt;[T](soft-depend.html) : Plugin&gt; [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html).[softDepend](soft-depend.html)(pluginName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PluginUtils.SoftDependencyDelegate](-soft-dependency-delegate/index.html)&lt;[T](soft-depend.html)&gt;<br>Delegate that returns the plugin dependency if the plugin is installed in the server otherwise, returns null<br>[jvm]<br>fun &lt;[T](soft-depend.html) : Plugin&gt; [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html).[softDepend](soft-depend.html)(type: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](soft-depend.html)&gt;, pluginName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PluginUtils.SoftDependencyDelegate](-soft-dependency-delegate/index.html)&lt;[T](soft-depend.html)&gt; |

