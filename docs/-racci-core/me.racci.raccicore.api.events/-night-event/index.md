//[RacciCore](../../../index.md)/[me.racci.raccicore.api.events](../index.md)/[NightEvent](index.md)

# NightEvent

[jvm]\
class [NightEvent](index.md)(world: World) : [KWorldEvent](../-k-world-event/index.md)

This event is fired when the world turns to Night.

## This method is Fired Asynchronously

## Parameters

jvm

| | |
|---|---|
| world | The world of the event. |

## Constructors

| | |
|---|---|
| [NightEvent](-night-event.md) | [jvm]<br>fun [NightEvent](-night-event.md)(world: World) |

## Functions

| Name | Summary |
|---|---|
| [callEvent](../-day-event/index.md#-1071638799%2FFunctions%2F-1216412040) | [jvm]<br>open fun [callEvent](../-day-event/index.md#-1071638799%2FFunctions%2F-1216412040)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getEventName](../-day-event/index.md#1147460734%2FFunctions%2F-1216412040) | [jvm]<br>@NotNull<br>open fun [getEventName](../-day-event/index.md#1147460734%2FFunctions%2F-1216412040)(): @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getHandlers](../-k-world-event/get-handlers.md) | [jvm]<br>open override fun [getHandlers](../-k-world-event/get-handlers.md)(): HandlerList |
| [getWorld](../-day-event/index.md#-2066259439%2FFunctions%2F-1216412040) | [jvm]<br>@NotNull<br>open fun [getWorld](../-day-event/index.md#-2066259439%2FFunctions%2F-1216412040)(): @NotNullWorld |
| [isAsynchronous](../-day-event/index.md#-706610981%2FFunctions%2F-1216412040) | [jvm]<br>fun [isAsynchronous](../-day-event/index.md#-706610981%2FFunctions%2F-1216412040)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isCancelled](../-k-world-event/is-cancelled.md) | [jvm]<br>open override fun [isCancelled](../-k-world-event/is-cancelled.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setCancelled](../-k-world-event/set-cancelled.md) | [jvm]<br>open override fun [setCancelled](../-k-world-event/set-cancelled.md)(cancel: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [isCustom](../-k-world-event/is-custom.md) | [jvm]<br>val [isCustom](../-k-world-event/is-custom.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isEnd](../-k-world-event/is-end.md) | [jvm]<br>val [isEnd](../-k-world-event/is-end.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isNether](../-k-world-event/is-nether.md) | [jvm]<br>val [isNether](../-k-world-event/is-nether.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isOverworld](../-k-world-event/is-overworld.md) | [jvm]<br>val [isOverworld](../-k-world-event/is-overworld.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
