---
title: playerEventFlow
---
//[RacciCore](../../index.html)/[me.racci.raccicore.api.flow](index.html)/[playerEventFlow](player-event-flow.html)



# playerEventFlow



[jvm]\
inline fun &lt;[T](player-event-flow.html) : PlayerEvent&gt; [WithPlugin](../me.racci.raccicore.api.extensions/-with-plugin/index.html)&lt;*&gt;.[playerEventFlow](player-event-flow.html)(player: Player, priority: EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, channel: Channel&lt;[T](player-event-flow.html)&gt; = Channel(Channel.CONFLATED), listener: Listener = SimpleKListener(plugin)): Flow&lt;[T](player-event-flow.html)&gt;



Creates an event flow for PlayerEvent that auto filter for only events from [player](player-event-flow.html).





[jvm]\
inline fun &lt;[T](player-event-flow.html) : PlayerEvent&gt; [RacciPlugin](../me.racci.raccicore.api.plugin/-racci-plugin/index.html).[playerEventFlow](player-event-flow.html)(player: Player, priority: EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, channel: Channel&lt;[T](player-event-flow.html)&gt; = Channel(Channel.CONFLATED), listener: Listener = SimpleKListener(this)): Flow&lt;[T](player-event-flow.html)&gt;

inline fun &lt;[T](player-event-flow.html) : PlayerEvent&gt; [playerEventFlow](player-event-flow.html)(player: Player, plugin: [RacciPlugin](../me.racci.raccicore.api.plugin/-racci-plugin/index.html), priority: EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, channel: Channel&lt;[T](player-event-flow.html)&gt; = Channel(Channel.CONFLATED), listener: Listener = SimpleKListener(plugin)): Flow&lt;[T](player-event-flow.html)&gt;




