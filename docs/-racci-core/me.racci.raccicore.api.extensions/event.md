---
title: event
---
//[RacciCore](../../index.html)/[me.racci.raccicore.api.extensions](index.html)/[event](event.html)



# event



[jvm]\
inline fun &lt;[T](event.html) : Event&gt; [KListener](-k-listener/index.html)&lt;*&gt;.[event](event.html)(priority: EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, noinline block: [T](event.html).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))

fun &lt;[T](event.html) : Event&gt; [KListener](-k-listener/index.html)&lt;*&gt;.[event](event.html)(type: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](event.html)&gt;, priority: EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, block: [T](event.html).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))

inline fun &lt;[T](event.html) : Event&gt; Listener.[event](event.html)(plugin: [RacciPlugin](../me.racci.raccicore.api.plugin/-racci-plugin/index.html), priority: EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, noinline block: [T](event.html).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))

fun &lt;[T](event.html) : Event&gt; Listener.[event](event.html)(plugin: [RacciPlugin](../me.racci.raccicore.api.plugin/-racci-plugin/index.html), type: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](event.html)&gt;, priority: EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, block: [T](event.html).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))




