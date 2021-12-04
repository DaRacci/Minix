//[RacciCore](../../../index.md)/[me.racci.raccicore.api.utils.minecraft](../index.md)/[PluginUtils](index.md)/[softDepend](soft-depend.md)

# softDepend

[jvm]\
inline fun &lt;[T](soft-depend.md) : Plugin&gt; [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md).[softDepend](soft-depend.md)(
pluginName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PluginUtils.SoftDependencyDelegate](-soft-dependency-delegate/index.md)&lt;[T](soft-depend.md)&gt;

Delegate that returns the plugin dependency if the plugin is installed in the server otherwise, returns null

[jvm]\
fun &lt;[T](soft-depend.md) : Plugin&gt; [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md).[softDepend](soft-depend.md)(type: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](soft-depend.md)&gt;,
pluginName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PluginUtils.SoftDependencyDelegate](-soft-dependency-delegate/index.md)&lt;[T](soft-depend.md)&gt;
