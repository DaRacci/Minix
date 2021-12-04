//[RacciCore](../../../../index.md)/[me.racci.raccicore.api.utils.minecraft](../../index.md)/[PluginUtils](../index.md)/[SoftDependencyDelegate](index.md)

# SoftDependencyDelegate

[jvm]\
class [SoftDependencyDelegate](index.md)&lt;[T](index.md) : Plugin&gt;(pluginName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html),
type: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](index.md)&gt;) : [ReadOnlyProperty](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.properties/-read-only-property/index.html)&lt;[RacciPlugin](
../../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), [T](index.md)?&gt;

## Functions

| Name | Summary |
|---|---|
| [getValue](get-value.md) | [jvm]<br>open operator override fun [getValue](get-value.md)(thisRef: [RacciPlugin](../../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), property: [KProperty](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property/index.html)&lt;*&gt;): [T](index.md)? |

## Properties

| Name | Summary |
|---|---|
| [pluginName](plugin-name.md) | [jvm]<br>val [pluginName](plugin-name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [type](type.md) | [jvm]<br>val [type](type.md): [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](index.md)&gt; |
