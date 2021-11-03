//[RacciCore](../../../index.md)/[me.racci.raccicore.events](../index.md)/[PlayerExitLiquidEvent](index.md)

# PlayerExitLiquidEvent

[jvm]\
class [PlayerExitLiquidEvent](index.md)(player: Player, liquidType: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), from: Location, to: Location) : [KotlinEvent](../-kotlin-event/index.md)

Player exit liquid event

## Constructors

| | |
|---|---|
| [PlayerExitLiquidEvent](-player-exit-liquid-event.md) | [jvm]<br>fun [PlayerExitLiquidEvent](-player-exit-liquid-event.md)(player: Player, liquidType: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), from: Location, to: Location)<br>Create empty Player exit liquid event |

## Functions

| Name | Summary |
|---|---|
| [callEvent](../-day-event/index.md#-1071638799%2FFunctions%2F-519281799) | [jvm]<br>open fun [callEvent](../-day-event/index.md#-1071638799%2FFunctions%2F-519281799)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getEventName](../-day-event/index.md#1147460734%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>open fun [getEventName](../-day-event/index.md#1147460734%2FFunctions%2F-519281799)(): @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getHandlers](../-kotlin-event/get-handlers.md) | [jvm]<br>open override fun [getHandlers](../-kotlin-event/get-handlers.md)(): HandlerList |
| [isAsynchronous](../-day-event/index.md#-706610981%2FFunctions%2F-519281799) | [jvm]<br>fun [isAsynchronous](../-day-event/index.md#-706610981%2FFunctions%2F-519281799)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isCancelled](../-kotlin-event/is-cancelled.md) | [jvm]<br>open override fun [isCancelled](../-kotlin-event/is-cancelled.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setCancelled](../-kotlin-event/set-cancelled.md) | [jvm]<br>open override fun [setCancelled](../-kotlin-event/set-cancelled.md)(cancel: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [from](from.md) | [jvm]<br>val [from](from.md): Location |
| [liquidType](liquid-type.md) | [jvm]<br>val [liquidType](liquid-type.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [player](player.md) | [jvm]<br>val [player](player.md): Player |
| [to](to.md) | [jvm]<br>val [to](to.md): Location |
