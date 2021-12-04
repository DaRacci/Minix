//[RacciCore](../../../index.md)/[me.racci.raccicore.api.extensions](../index.md)/[KListener](index.md)

# KListener

[jvm]\
interface [KListener](index.md)&lt;[T](index.md) : [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md)&gt; : Listener, [WithPlugin](../-with-plugin/index.md)&lt;[T](index.md)&gt;

## Properties

| Name | Summary |
|---|---|
| [plugin](../-with-plugin/plugin.md) | [jvm]<br>abstract val [plugin](../-with-plugin/plugin.md): [T](index.md) |

## Inheritors

| Name |
|---|
| [SimpleKListener](../-simple-k-listener/index.md) |
| [OnlinePlayerCollection](../../me.racci.raccicore.api.utils.collections/-online-player-collection/index.md) |
| [OnlinePlayerMap](../../me.racci.raccicore.api.utils.collections/-online-player-map/index.md) |
| [PluginManager](../../me.racci.raccicore.core/-plugin-manager/index.md) |
| [CommandManager](../../me.racci.raccicore.core.managers/-command-manager/index.md) |
| [PlayerManager](../../me.racci.raccicore.core.managers/-player-manager/index.md) |

## Extensions

| Name | Summary |
|---|---|
| [event](../event.md) | [jvm]<br>inline fun &lt;[T](../event.md) : Event&gt; [KListener](index.md)&lt;*&gt;.[event](../event.md)(priority: EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) =
false, noinline block: [T](../event.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))<br>fun &lt;[T](../event.md) : Event&gt; [KListener](index.md)
&lt;*&gt;.[event](../event.md)(type: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](../event.md)&gt;, priority: EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, block: [T](../event.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
