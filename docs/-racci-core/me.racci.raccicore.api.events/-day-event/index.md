---
title: DayEvent
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.events](../index.html)/[DayEvent](index.html)



# DayEvent



[jvm]\
class [DayEvent](index.html)(world: World) : [KWorldEvent](../-k-world-event/index.html)

This event is fired when the world turns to Day.



##  This method is Fired Asynchronously



## Parameters


jvm

| | |
|---|---|
| world | The world of the event. |



## Constructors


| | |
|---|---|
| [DayEvent](-day-event.html) | [jvm]<br>fun [DayEvent](-day-event.html)(world: World) |


## Functions


| Name | Summary |
|---|---|
| [callEvent](index.html#-1071638799%2FFunctions%2F863300109) | [jvm]<br>open fun [callEvent](index.html#-1071638799%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getEventName](index.html#1147460734%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>open fun [getEventName](index.html#1147460734%2FFunctions%2F863300109)(): @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getHandlers](../-k-world-event/get-handlers.html) | [jvm]<br>open override fun [getHandlers](../-k-world-event/get-handlers.html)(): HandlerList |
| [getWorld](index.html#-2066259439%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>open fun [getWorld](index.html#-2066259439%2FFunctions%2F863300109)(): @NotNullWorld |
| [isAsynchronous](index.html#-706610981%2FFunctions%2F863300109) | [jvm]<br>fun [isAsynchronous](index.html#-706610981%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isCancelled](../-k-world-event/is-cancelled.html) | [jvm]<br>open override fun [isCancelled](../-k-world-event/is-cancelled.html)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setCancelled](../-k-world-event/set-cancelled.html) | [jvm]<br>open override fun [setCancelled](../-k-world-event/set-cancelled.html)(cancel: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |


## Properties


| Name | Summary |
|---|---|
| [isCustom](../-k-world-event/is-custom.html) | [jvm]<br>val [isCustom](../-k-world-event/is-custom.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isEnd](../-k-world-event/is-end.html) | [jvm]<br>val [isEnd](../-k-world-event/is-end.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isNether](../-k-world-event/is-nether.html) | [jvm]<br>val [isNether](../-k-world-event/is-nether.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isOverworld](../-k-world-event/is-overworld.html) | [jvm]<br>val [isOverworld](../-k-world-event/is-overworld.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

