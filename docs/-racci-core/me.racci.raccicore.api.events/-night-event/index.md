---
title: NightEvent
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.events](../index.html)/[NightEvent](index.html)



# NightEvent



[jvm]\
class [NightEvent](index.html)(world: World) : [KWorldEvent](../-k-world-event/index.html)

This event is fired when the world turns to Night.



##  This method is Fired Asynchronously



## Parameters


jvm

| | |
|---|---|
| world | The world of the event. |



## Constructors


| | |
|---|---|
| [NightEvent](-night-event.html) | [jvm]<br>fun [NightEvent](-night-event.html)(world: World) |


## Functions


| Name | Summary |
|---|---|
| [callEvent](../-day-event/index.html#-1071638799%2FFunctions%2F863300109) | [jvm]<br>open fun [callEvent](../-day-event/index.html#-1071638799%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getEventName](../-day-event/index.html#1147460734%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>open fun [getEventName](../-day-event/index.html#1147460734%2FFunctions%2F863300109)(): @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getHandlers](../-k-world-event/get-handlers.html) | [jvm]<br>open override fun [getHandlers](../-k-world-event/get-handlers.html)(): HandlerList |
| [getWorld](../-day-event/index.html#-2066259439%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>open fun [getWorld](../-day-event/index.html#-2066259439%2FFunctions%2F863300109)(): @NotNullWorld |
| [isAsynchronous](../-day-event/index.html#-706610981%2FFunctions%2F863300109) | [jvm]<br>fun [isAsynchronous](../-day-event/index.html#-706610981%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isCancelled](../-k-world-event/is-cancelled.html) | [jvm]<br>open override fun [isCancelled](../-k-world-event/is-cancelled.html)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setCancelled](../-k-world-event/set-cancelled.html) | [jvm]<br>open override fun [setCancelled](../-k-world-event/set-cancelled.html)(cancel: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |


## Properties


| Name | Summary |
|---|---|
| [isCustom](../-k-world-event/is-custom.html) | [jvm]<br>val [isCustom](../-k-world-event/is-custom.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isEnd](../-k-world-event/is-end.html) | [jvm]<br>val [isEnd](../-k-world-event/is-end.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isNether](../-k-world-event/is-nether.html) | [jvm]<br>val [isNether](../-k-world-event/is-nether.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isOverworld](../-k-world-event/is-overworld.html) | [jvm]<br>val [isOverworld](../-k-world-event/is-overworld.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

