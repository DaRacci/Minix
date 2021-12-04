//[RacciCore](../../../index.md)/[me.racci.raccicore.api.utils.minecraft](../index.md)/[PluginUtils](index.md)

# PluginUtils

[jvm]\
object [PluginUtils](index.md)

## Types

| Name | Summary |
|---|---|
| [DependencyDelegate](-dependency-delegate/index.md) | [jvm]<br>class [DependencyDelegate](-dependency-delegate/index.md)&lt;[T](-dependency-delegate/index.md) : Plugin&gt;(pluginName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), type: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](-dependency-delegate/index.md)&gt;) : [ReadOnlyProperty](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.properties/-read-only-property/index.html)&lt;[RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), [T](-dependency-delegate/index.md)&gt; |
| [SoftDependencyDelegate](-soft-dependency-delegate/index.md) | [jvm]<br>class [SoftDependencyDelegate](-soft-dependency-delegate/index.md)&lt;[T](-soft-dependency-delegate/index.md) : Plugin&gt;(pluginName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), type: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](-soft-dependency-delegate/index.md)&gt;) : [ReadOnlyProperty](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.properties/-read-only-property/index.html)&lt;[RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), [T](-soft-dependency-delegate/index.md)?&gt; |

## Functions

| Name | Summary |
|---|---|
| [depend](depend.md) | [jvm]<br>inline fun &lt;[T](depend.md) : Plugin&gt; [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md).[depend](depend.md)(pluginName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PluginUtils.DependencyDelegate](-dependency-delegate/index.md)&lt;[T](depend.md)&gt;<br>Delegate that returns the plugin dependency, disable the plugin if the plugin is not available.<br>[jvm]<br>fun &lt;[T](depend.md) : Plugin&gt; [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md).[depend](depend.md)(type: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](depend.md)&gt;, pluginName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PluginUtils.DependencyDelegate](-dependency-delegate/index.md)&lt;[T](depend.md)&gt; |
| [softDepend](soft-depend.md) | [jvm]<br>inline fun &lt;[T](soft-depend.md) : Plugin&gt; [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md).[softDepend](soft-depend.md)(pluginName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PluginUtils.SoftDependencyDelegate](-soft-dependency-delegate/index.md)&lt;[T](soft-depend.md)&gt;<br>Delegate that returns the plugin dependency if the plugin is installed in the server otherwise, returns null<br>[jvm]<br>fun &lt;[T](soft-depend.md) : Plugin&gt; [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md).[softDepend](soft-depend.md)(type: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](soft-depend.md)&gt;, pluginName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PluginUtils.SoftDependencyDelegate](-soft-dependency-delegate/index.md)&lt;[T](soft-depend.md)&gt; |
