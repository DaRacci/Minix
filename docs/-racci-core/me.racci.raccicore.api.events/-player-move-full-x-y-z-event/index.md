---
title: PlayerMoveFullXYZEvent
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.events](../index.html)/[PlayerMoveFullXYZEvent](index.html)



# PlayerMoveFullXYZEvent



[jvm]\
class [PlayerMoveFullXYZEvent](index.html)(player: Player, from: Location, to: Location) : [KPlayerEvent](../-k-player-event/index.html)

This event is fired when the player moves one full block, So if the player moves +1 in any direction this will apply.



##  This method is Fired Asynchronously



## Constructors


| | |
|---|---|
| [PlayerMoveFullXYZEvent](-player-move-full-x-y-z-event.html) | [jvm]<br>fun [PlayerMoveFullXYZEvent](-player-move-full-x-y-z-event.html)(player: Player, from: Location, to: Location) |


## Functions


| Name | Summary |
|---|---|
| [callEvent](../-day-event/index.html#-1071638799%2FFunctions%2F863300109) | [jvm]<br>open fun [callEvent](../-day-event/index.html#-1071638799%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getEventName](../-day-event/index.html#1147460734%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>open fun [getEventName](../-day-event/index.html#1147460734%2FFunctions%2F863300109)(): @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getHandlers](../-k-player-event/get-handlers.html) | [jvm]<br>open override fun [getHandlers](../-k-player-event/get-handlers.html)(): HandlerList |
| [getPlayer](index.html#-1478213936%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>fun [getPlayer](index.html#-1478213936%2FFunctions%2F863300109)(): @NotNullPlayer |
| [isAsynchronous](../-day-event/index.html#-706610981%2FFunctions%2F863300109) | [jvm]<br>fun [isAsynchronous](../-day-event/index.html#-706610981%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isCancelled](../-k-player-event/is-cancelled.html) | [jvm]<br>open override fun [isCancelled](../-k-player-event/is-cancelled.html)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setCancelled](../-k-player-event/set-cancelled.html) | [jvm]<br>open override fun [setCancelled](../-k-player-event/set-cancelled.html)(cancel: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |


## Properties


| Name | Summary |
|---|---|
| [from](from.html) | [jvm]<br>val [from](from.html): Location<br>The previous location the player was at. |
| [player](index.html#-8709326%2FProperties%2F863300109) | [jvm]<br>val [player](index.html#-8709326%2FProperties%2F863300109): Player |
| [to](to.html) | [jvm]<br>var [to](to.html): Location<br>The players new location. |

