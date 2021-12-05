---
title: KPlayerEvent
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.events](../index.html)/[KPlayerEvent](index.html)



# KPlayerEvent



[jvm]\
abstract class [KPlayerEvent](index.html)(player: Player, async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : PlayerEvent, Cancellable

Represents a player event.



##  Includes Pre done handlers and cancellable params



## Parameters


jvm

| | |
|---|---|
| player | The player of the event. |
| async | If the event is Asynchronous. |



## Constructors


| | |
|---|---|
| [KPlayerEvent](-k-player-event.html) | [jvm]<br>fun [KPlayerEvent](-k-player-event.html)(player: Player, async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false) |


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
| [getPlayer](../-player-move-full-x-y-z-event/index.html#-1478213936%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>fun [getPlayer](../-player-move-full-x-y-z-event/index.html#-1478213936%2FFunctions%2F863300109)(): @NotNullPlayer |
| [isAsynchronous](../-day-event/index.html#-706610981%2FFunctions%2F863300109) | [jvm]<br>fun [isAsynchronous](../-day-event/index.html#-706610981%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isCancelled](is-cancelled.html) | [jvm]<br>open override fun [isCancelled](is-cancelled.html)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setCancelled](set-cancelled.html) | [jvm]<br>open override fun [setCancelled](set-cancelled.html)(cancel: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |


## Properties


| Name | Summary |
|---|---|
| [player](../-player-move-full-x-y-z-event/index.html#-8709326%2FProperties%2F863300109) | [jvm]<br>val [player](../-player-move-full-x-y-z-event/index.html#-8709326%2FProperties%2F863300109): Player |


## Inheritors


| Name |
|---|
| [AbstractComboEvent](../-abstract-combo-event/index.html) |
| [PlayerEnterLiquidEvent](../-player-enter-liquid-event/index.html) |
| [PlayerExitLiquidEvent](../-player-exit-liquid-event/index.html) |
| [PlayerMoveXYZEvent](../-player-move-x-y-z-event/index.html) |
| [PlayerMoveFullXYZEvent](../-player-move-full-x-y-z-event/index.html) |

