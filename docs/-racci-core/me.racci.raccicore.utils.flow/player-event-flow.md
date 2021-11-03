//[RacciCore](../../index.md)/[me.racci.raccicore.utils.flow](index.md)/[playerEventFlow](player-event-flow.md)

# playerEventFlow

[jvm]\
inline fun &lt;[T](player-event-flow.md) : PlayerEvent&gt; [WithPlugin](../me.racci.raccicore.utils.extensions/-with-plugin/index.md)&lt;*&gt;.[playerEventFlow](player-event-flow.md)(player: Player, priority: EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, channel: Channel&lt;[T](player-event-flow.md)&gt; = Channel(Channel.CONFLATED), listener: Listener = SimpleKListener(plugin)): Flow&lt;[T](player-event-flow.md)&gt;

Creates a event flow for PlayerEvent that auto filter for only events from [player](player-event-flow.md).

[jvm]\
inline fun &lt;[T](player-event-flow.md) : PlayerEvent&gt; Plugin.[playerEventFlow](player-event-flow.md)(player: Player, priority: EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, channel: Channel&lt;[T](player-event-flow.md)&gt; = Channel(Channel.CONFLATED), listener: Listener = SimpleKListener(this)): Flow&lt;[T](player-event-flow.md)&gt;

inline fun &lt;[T](player-event-flow.md) : PlayerEvent&gt; [playerEventFlow](player-event-flow.md)(player: Player, plugin: Plugin, priority: EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, channel: Channel&lt;[T](player-event-flow.md)&gt; = Channel(Channel.CONFLATED), listener: Listener = SimpleKListener(plugin)): Flow&lt;[T](player-event-flow.md)&gt;
