//[RacciCore](../../../index.md)/[me.racci.raccicore.skedule](../index.md)/[SynchronizationContext](index.md)

# SynchronizationContext

[jvm]\
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")

~~enum~~ [~~SynchronizationContext~~](index.md) ~~:~~ [~~Enum~~](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)~~&lt;~~[SynchronizationContext](index.md)~~&gt;~~ 

Represents a synchronization context that a BukkitScheduler coroutine is currently in.

## Entries

| | |
|---|---|
| [ASYNC](-a-s-y-n-c/index.md) | [jvm]<br>[ASYNC](-a-s-y-n-c/index.md)()<br>The coroutine is in asynchronous context, and all tasks are scheduled asynchronously to the main server thread. |
| [SYNC](-s-y-n-c/index.md) | [jvm]<br>[SYNC](-s-y-n-c/index.md)()<br>The coroutine is in synchronous context, and all tasks are scheduled on the main server thread |

## Properties

| Name | Summary |
|---|---|
| [name](../../me.racci.raccicore.utils.collections/-observable-action/-a-d-d/index.md#-372974862%2FProperties%2F-519281799) | [jvm]<br>val [name](../../me.racci.raccicore.utils.collections/-observable-action/-a-d-d/index.md#-372974862%2FProperties%2F-519281799): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [ordinal](../../me.racci.raccicore.utils.collections/-observable-action/-a-d-d/index.md#-739389684%2FProperties%2F-519281799) | [jvm]<br>val [ordinal](../../me.racci.raccicore.utils.collections/-observable-action/-a-d-d/index.md#-739389684%2FProperties%2F-519281799): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
