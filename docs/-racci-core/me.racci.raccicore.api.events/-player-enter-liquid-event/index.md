---
title: PlayerEnterLiquidEvent
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.events](../index.html)/[PlayerEnterLiquidEvent](index.html)



# PlayerEnterLiquidEvent



[jvm]\
class [PlayerEnterLiquidEvent](index.html)(player: Player, liquidType: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), from: Location, to: Location) : [KPlayerEvent](../-k-player-event/index.html)

This event is fires when the player enters water, lava or a waterlogged block.



##  This method is Fired Asynchronously



## Parameters


jvm

| | |
|---|---|
| player | The player of the event. |
| liquidType | Material.WATER if it was water or a Waterlogged block, Material.LAVA if it was Lava |



## Constructors


| | |
|---|---|
| [PlayerEnterLiquidEvent](-player-enter-liquid-event.html) | [jvm]<br>fun [PlayerEnterLiquidEvent](-player-enter-liquid-event.html)(player: Player, liquidType: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), from: Location, to: Location) |


## Functions


| Name | Summary |
|---|---|
| [callEvent](../-day-event/index.html#-1071638799%2FFunctions%2F863300109) | [jvm]<br>open fun [callEvent](../-day-event/index.html#-1071638799%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getEventName](../-day-event/index.html#1147460734%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>open fun [getEventName](../-day-event/index.html#1147460734%2FFunctions%2F863300109)(): @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getHandlers](../-k-player-event/get-handlers.html) | [jvm]<br>open override fun [getHandlers](../-k-player-event/get-handlers.html)(): HandlerList |
| [getPlayer](../-player-move-full-x-y-z-event/index.html#-1478213936%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>fun [getPlayer](../-player-move-full-x-y-z-event/index.html#-1478213936%2FFunctions%2F863300109)(): @NotNullPlayer |
| [isAsynchronous](../-day-event/index.html#-706610981%2FFunctions%2F863300109) | [jvm]<br>fun [isAsynchronous](../-day-event/index.html#-706610981%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isCancelled](../-k-player-event/is-cancelled.html) | [jvm]<br>open override fun [isCancelled](../-k-player-event/is-cancelled.html)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setCancelled](../-k-player-event/set-cancelled.html) | [jvm]<br>open override fun [setCancelled](../-k-player-event/set-cancelled.html)(cancel: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |


## Properties


| Name | Summary |
|---|---|
| [from](from.html) | [jvm]<br>val [from](from.html): Location<br>The previous block where the player was before entering the liquid. |
| [liquidType](liquid-type.html) | [jvm]<br>val [liquidType](liquid-type.html): Material |
| [player](../-player-move-full-x-y-z-event/index.html#-8709326%2FProperties%2F863300109) | [jvm]<br>val [player](../-player-move-full-x-y-z-event/index.html#-8709326%2FProperties%2F863300109): Player |
| [to](to.html) | [jvm]<br>val [to](to.html): Location<br>The liquid block the player has entered. |

