---
title: depend
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.minecraft](../index.html)/[PluginUtils](index.html)/[depend](depend.html)



# depend



[jvm]\
inline fun &lt;[T](depend.html) : Plugin&gt; [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html).[depend](depend.html)(pluginName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PluginUtils.DependencyDelegate](-dependency-delegate/index.html)&lt;[T](depend.html)&gt;



Delegate that returns the plugin dependency, disable the plugin if the plugin is not available.





[jvm]\
fun &lt;[T](depend.html) : Plugin&gt; [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html).[depend](depend.html)(type: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](depend.html)&gt;, pluginName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PluginUtils.DependencyDelegate](-dependency-delegate/index.html)&lt;[T](depend.html)&gt;




