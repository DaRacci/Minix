//[RacciCore](../../index.md)/[me.racci.raccicore.utils.flow](index.md)/[eventFlow](event-flow.md)

# eventFlow

[jvm]\
inline fun &lt;[T](event-flow.md) : Event&gt; [WithPlugin](../me.racci.raccicore.utils.extensions/-with-plugin/index.md)&lt;*&gt;.[eventFlow](event-flow.md)(assign: Player? = null, priority: EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, channel: Channel&lt;[T](event-flow.md)&gt; = Channel(Channel.CONFLATED), listener: Listener = plugin.events {}, assignListener: Listener = plugin.events {}): Flow&lt;[T](event-flow.md)&gt;

inline fun &lt;[T](event-flow.md) : Event&gt; Plugin.[eventFlow](event-flow.md)(assign: Player? = null, priority: EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, channel: Channel&lt;[T](event-flow.md)&gt; = Channel(Channel.CONFLATED), listener: Listener = events {}, assignListener: Listener = events {}): Flow&lt;[T](event-flow.md)&gt;

[jvm]\
fun &lt;[T](event-flow.md) : Event&gt; [eventFlow](event-flow.md)(type: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](event-flow.md)&gt;, plugin: Plugin, assign: Player? = null, priority: EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, channel: Channel&lt;[T](event-flow.md)&gt; = Channel(Channel.CONFLATED), listener: Listener = SimpleKListener(plugin), assignListener: Listener = SimpleKListener(plugin)): Flow&lt;[T](event-flow.md)&gt;

Create a Event Flow that receives the specified Event [type](event-flow.md).

[assign](event-flow.md) is use for auto cancel the Flow when the Player disconnects.
