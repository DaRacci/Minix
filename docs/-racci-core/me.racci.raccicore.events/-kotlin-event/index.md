//[RacciCore](../../../index.md)/[me.racci.raccicore.events](../index.md)/[KotlinEvent](index.md)

# KotlinEvent

[jvm]\
abstract class [KotlinEvent](index.md)(async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : Event, Cancellable

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [callEvent](../-day-event/index.md#-1071638799%2FFunctions%2F-1216412040) | [jvm]<br>open fun [callEvent](../-day-event/index.md#-1071638799%2FFunctions%2F-1216412040)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getEventName](../-day-event/index.md#1147460734%2FFunctions%2F-1216412040) | [jvm]<br>@NotNull<br>open fun [getEventName](../-day-event/index.md#1147460734%2FFunctions%2F-1216412040)(): @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getHandlers](get-handlers.md) | [jvm]<br>open override fun [getHandlers](get-handlers.md)(): HandlerList |
| [isAsynchronous](../-day-event/index.md#-706610981%2FFunctions%2F-1216412040) | [jvm]<br>fun [isAsynchronous](../-day-event/index.md#-706610981%2FFunctions%2F-1216412040)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isCancelled](is-cancelled.md) | [jvm]<br>open override fun [isCancelled](is-cancelled.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setCancelled](set-cancelled.md) | [jvm]<br>open override fun [setCancelled](set-cancelled.md)(cancel: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |

## Inheritors

| Name |
|---|
| [PlayerShiftLeftClickEvent](../-player-shift-left-click-event/index.md) |
| [PlayerShiftRightClickEvent](../-player-shift-right-click-event/index.md) |
| [PlayerShiftOffhandEvent](../-player-shift-offhand-event/index.md) |
| [PlayerShiftDoubleOffhandEvent](../-player-shift-double-offhand-event/index.md) |
| [PlayerLeftClickEvent](../-player-left-click-event/index.md) |
| [PlayerRightClickEvent](../-player-right-click-event/index.md) |
| [PlayerOffhandEvent](../-player-offhand-event/index.md) |
| [PlayerDoubleOffhandEvent](../-player-double-offhand-event/index.md) |
| [PlayerEnterLiquidEvent](../-player-enter-liquid-event/index.md) |
| [PlayerExitLiquidEvent](../-player-exit-liquid-event/index.md) |
| [PlayerMoveXYZEvent](../-player-move-x-y-z-event/index.md) |
| [PlayerMoveFullXYZEvent](../-player-move-full-x-y-z-event/index.md) |
| [NightEvent](../-night-event/index.md) |
| [DayEvent](../-day-event/index.md) |
