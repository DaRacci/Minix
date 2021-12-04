//[RacciCore](../../../index.md)/[me.racci.raccicore.api.utils.minecraft](../index.md)/[PluginUtils](index.md)/[depend](depend.md)

# depend

[jvm]\
inline fun &lt;[T](depend.md) : Plugin&gt; [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md).[depend](depend.md)(
pluginName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PluginUtils.DependencyDelegate](-dependency-delegate/index.md)&lt;[T](depend.md)&gt;

Delegate that returns the plugin dependency, disable the plugin if the plugin is not available.

[jvm]\
fun &lt;[T](depend.md) : Plugin&gt; [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md).[depend](depend.md)(type: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](depend.md)&gt;,
pluginName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PluginUtils.DependencyDelegate](-dependency-delegate/index.md)&lt;[T](depend.md)&gt;
