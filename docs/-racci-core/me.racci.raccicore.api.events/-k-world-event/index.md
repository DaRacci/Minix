---
title: KWorldEvent
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.events](../index.html)/[KWorldEvent](index.html)



# KWorldEvent



[jvm]\
abstract class [KWorldEvent](index.html)(world: World, async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : WorldEvent, Cancellable

Represents a World event, Includes variables [isOverworld](is-nether.html) and [isEnd](is-end.html) These make it easy to find what world type the event is within.



##  Includes Pre done handlers and cancellable params



## Parameters


jvm

| | |
|---|---|
| world | The world which this event happened in. |
| async | If the event is Asynchronous, |



## Constructors


| | |
|---|---|
| [KWorldEvent](-k-world-event.html) | [jvm]<br>fun [KWorldEvent](-k-world-event.html)(world: World, async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false) |


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
| [getWorld](../-day-event/index.html#-2066259439%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>open fun [getWorld](../-day-event/index.html#-2066259439%2FFunctions%2F863300109)(): @NotNullWorld |
| [isAsynchronous](../-day-event/index.html#-706610981%2FFunctions%2F863300109) | [jvm]<br>fun [isAsynchronous](../-day-event/index.html#-706610981%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isCancelled](is-cancelled.html) | [jvm]<br>open override fun [isCancelled](is-cancelled.html)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setCancelled](set-cancelled.html) | [jvm]<br>open override fun [setCancelled](set-cancelled.html)(cancel: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |


## Properties


| Name | Summary |
|---|---|
| [isCustom](is-custom.html) | [jvm]<br>val [isCustom](is-custom.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isEnd](is-end.html) | [jvm]<br>val [isEnd](is-end.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isNether](is-nether.html) | [jvm]<br>val [isNether](is-nether.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isOverworld](is-overworld.html) | [jvm]<br>val [isOverworld](is-overworld.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |


## Inheritors


| Name |
|---|
| [NightEvent](../-night-event/index.html) |
| [DayEvent](../-day-event/index.html) |

