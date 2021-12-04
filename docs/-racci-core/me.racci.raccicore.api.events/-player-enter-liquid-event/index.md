//[RacciCore](../../../index.md)/[me.racci.raccicore.api.events](../index.md)/[PlayerEnterLiquidEvent](index.md)

# PlayerEnterLiquidEvent

[jvm]\
class [PlayerEnterLiquidEvent](index.md)(player: Player, liquidType: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), from: Location, to: Location) : [KPlayerEvent](../-k-player-event/index.md)

This event is fires when the player enters water, lava or a waterlogged block.

## This method is Fired Asynchronously

## Parameters

jvm

| | |
|---|---|
| player | The player of the event. |
| liquidType | Material.WATER if it was water or a Waterlogged block, Material.LAVA if it was Lava |

## Constructors

| | |
|---|---|
| [PlayerEnterLiquidEvent](-player-enter-liquid-event.md) | [jvm]<br>fun [PlayerEnterLiquidEvent](-player-enter-liquid-event.md)(player: Player, liquidType: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), from: Location, to: Location) |

## Functions

| Name | Summary |
|---|---|
| [callEvent](../-day-event/index.md#-1071638799%2FFunctions%2F-1216412040) | [jvm]<br>open fun [callEvent](../-day-event/index.md#-1071638799%2FFunctions%2F-1216412040)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getEventName](../-day-event/index.md#1147460734%2FFunctions%2F-1216412040) | [jvm]<br>@NotNull<br>open fun [getEventName](../-day-event/index.md#1147460734%2FFunctions%2F-1216412040)(): @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getHandlers](../-k-player-event/get-handlers.md) | [jvm]<br>open override fun [getHandlers](../-k-player-event/get-handlers.md)(): HandlerList |
| [getPlayer](../-player-move-full-x-y-z-event/index.md#-1478213936%2FFunctions%2F-1216412040) | [jvm]<br>@NotNull<br>fun [getPlayer](../-player-move-full-x-y-z-event/index.md#-1478213936%2FFunctions%2F-1216412040)(): @NotNullPlayer |
| [isAsynchronous](../-day-event/index.md#-706610981%2FFunctions%2F-1216412040) | [jvm]<br>fun [isAsynchronous](../-day-event/index.md#-706610981%2FFunctions%2F-1216412040)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isCancelled](../-k-player-event/is-cancelled.md) | [jvm]<br>open override fun [isCancelled](../-k-player-event/is-cancelled.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setCancelled](../-k-player-event/set-cancelled.md) | [jvm]<br>open override fun [setCancelled](../-k-player-event/set-cancelled.md)(cancel: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [from](from.md) | [jvm]<br>val [from](from.md): Location<br>The previous block where the player was before entering the liquid. |
| [liquidType](liquid-type.md) | [jvm]<br>val [liquidType](liquid-type.md): Material |
| [player](../-player-move-full-x-y-z-event/index.md#-8709326%2FProperties%2F-1216412040) | [jvm]<br>val [player](../-player-move-full-x-y-z-event/index.md#-8709326%2FProperties%2F-1216412040): Player |
| [to](to.md) | [jvm]<br>val [to](to.md): Location<br>The liquid block the player has entered. |
