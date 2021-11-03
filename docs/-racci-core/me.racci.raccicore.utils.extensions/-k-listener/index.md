//[RacciCore](../../../index.md)/[me.racci.raccicore.utils.extensions](../index.md)/[KListener](index.md)

# KListener

[jvm]\
interface [KListener](index.md)&lt;[T](index.md) : Plugin&gt; : Listener, [WithPlugin](../-with-plugin/index.md)&lt;[T](index.md)&gt;

## Properties

| Name | Summary |
|---|---|
| [plugin](../-with-plugin/plugin.md) | [jvm]<br>abstract val [plugin](../-with-plugin/plugin.md): [T](index.md) |

## Inheritors

| Name |
|---|
| [OnlinePlayerCollection](../../me.racci.raccicore.utils.collections/-online-player-collection/index.md) |
| [OnlinePlayerMap](../../me.racci.raccicore.utils.collections/-online-player-map/index.md) |
| [SimpleKListener](../-simple-k-listener/index.md) |

## Extensions

| Name | Summary |
|---|---|
| [event](../event.md) | [jvm]<br>inline fun &lt;[T](../event.md) : Event&gt; [KListener](index.md)&lt;*&gt;.[event](../event.md)(priority: EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, noinline block: [T](../event.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))<br>fun &lt;[T](../event.md) : Event&gt; [KListener](index.md)&lt;*&gt;.[event](../event.md)(type: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](../event.md)&gt;, priority: EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, block: [T](../event.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
