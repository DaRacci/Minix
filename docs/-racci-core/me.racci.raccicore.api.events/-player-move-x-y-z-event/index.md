//[RacciCore](../../../index.md)/[me.racci.raccicore.api.events](../index.md)/[PlayerMoveXYZEvent](index.md)

# PlayerMoveXYZEvent

[jvm]\
class [PlayerMoveXYZEvent](index.md)(player: Player, from: Location, to: Location) : [KPlayerEvent](../-k-player-event/index.md)

This event is fired only when the player moves, this means that unlike the normal PlayerMoveEvent it does not fire when the player looks around.

## This method is Fired Asynchronously

## Constructors

| | |
|---|---|
| [PlayerMoveXYZEvent](-player-move-x-y-z-event.md) | [jvm]<br>fun [PlayerMoveXYZEvent](-player-move-x-y-z-event.md)(player: Player, from: Location, to: Location) |

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
| [from](from.md) | [jvm]<br>val [from](from.md): Location<br>The previous location the player was at. |
| [player](../-player-move-full-x-y-z-event/index.md#-8709326%2FProperties%2F-1216412040) | [jvm]<br>val [player](../-player-move-full-x-y-z-event/index.md#-8709326%2FProperties%2F-1216412040): Player |
| [to](to.md) | [jvm]<br>var [to](to.md): Location<br>The players new location. |
