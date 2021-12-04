//[RacciCore](../../index.md)/[me.racci.raccicore.api.extensions](index.md)/[event](event.md)

# event

[jvm]\
inline fun &lt;[T](event.md) : Event&gt; [KListener](-k-listener/index.md)&lt;*&gt;.[event](event.md)(priority: EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, noinline
block: [T](event.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))

fun &lt;[T](event.md) : Event&gt; [KListener](-k-listener/index.md)&lt;*&gt;.[event](event.md)(type: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](event.md)&gt;, priority: EventPriority = EventPriority.NORMAL,
ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, block: [T](event.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))

inline fun &lt;[T](event.md) : Event&gt; Listener.[event](event.md)(plugin: [RacciPlugin](../me.racci.raccicore.api.plugin/-racci-plugin/index.md), priority: EventPriority = EventPriority.NORMAL,
ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, noinline block: [T](event.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))

fun &lt;[T](event.md) : Event&gt; Listener.[event](event.md)(plugin: [RacciPlugin](../me.racci.raccicore.api.plugin/-racci-plugin/index.md), type: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](event.md)&gt;, priority:
EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, block: [T](event.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))
