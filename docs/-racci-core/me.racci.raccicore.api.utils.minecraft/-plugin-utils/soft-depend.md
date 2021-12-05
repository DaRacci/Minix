---
title: softDepend
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.minecraft](../index.html)/[PluginUtils](index.html)/[softDepend](soft-depend.html)



# softDepend



[jvm]\
inline fun &lt;[T](soft-depend.html) : Plugin&gt; [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html).[softDepend](soft-depend.html)(pluginName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PluginUtils.SoftDependencyDelegate](-soft-dependency-delegate/index.html)&lt;[T](soft-depend.html)&gt;



Delegate that returns the plugin dependency if the plugin is installed in the server otherwise, returns null





[jvm]\
fun &lt;[T](soft-depend.html) : Plugin&gt; [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html).[softDepend](soft-depend.html)(type: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](soft-depend.html)&gt;, pluginName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PluginUtils.SoftDependencyDelegate](-soft-dependency-delegate/index.html)&lt;[T](soft-depend.html)&gt;




