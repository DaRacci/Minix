---
title: KEvent
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.events](../index.html)/[KEvent](index.html)



# KEvent



[jvm]\
abstract class [KEvent](index.html)(async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : Event, Cancellable

Represents an event.



##  Includes Pre done handlers and cancellable params



## Parameters


jvm

| | |
|---|---|
| async | If the event is Asynchronous. |



## Constructors


| | |
|---|---|
| [KEvent](-k-event.html) | [jvm]<br>fun [KEvent](-k-event.html)(async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false) |


## Types


| Name | Summary |
|---|---|
| [Companion](-companion/index.html) | [jvm]<br>object [Companion](-companion/index.html) |


## Functions


| Name | Summary |
|---|---|
| [callEvent](../-day-event/index.html#-1071638799%2FFunctions%2F863300109) | [jvm]<br>open fun [callEvent](../-day-event/index.html#-1071638799%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getEventName](../-day-event/index.html#1147460734%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>open fun [getEventName](../-day-event/index.html#1147460734%2FFunctions%2F863300109)(): @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getHandlers](get-handlers.html) | [jvm]<br>open override fun [getHandlers](get-handlers.html)(): HandlerList |
| [isAsynchronous](../-day-event/index.html#-706610981%2FFunctions%2F863300109) | [jvm]<br>fun [isAsynchronous](../-day-event/index.html#-706610981%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isCancelled](is-cancelled.html) | [jvm]<br>open override fun [isCancelled](is-cancelled.html)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setCancelled](set-cancelled.html) | [jvm]<br>open override fun [setCancelled](set-cancelled.html)(cancel: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |


## Inheritors


| Name |
|---|
| [PlayerUnloadEvent](../-player-unload-event/index.html) |

