//[RacciCore](../../../index.md)/[me.racci.raccicore.api.events](../index.md)/[KWorldEvent](index.md)

# KWorldEvent

[jvm]\
abstract class [KWorldEvent](index.md)(world: World, async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : WorldEvent, Cancellable

Represents a World event, Includes variables [isOverworld](is-nether.md) and [isEnd](is-end.md) These make it easy to find what world type the event is within.

## Includes Pre done handlers and cancellable params

## Parameters

jvm

| | |
|---|---|
| world | The world which this event happened in. |
| async | If the event is Asynchronous, |

## Constructors

| | |
|---|---|
| [KWorldEvent](-k-world-event.md) | [jvm]<br>fun [KWorldEvent](-k-world-event.md)(world: World, async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false) |

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
| [getWorld](../-day-event/index.md#-2066259439%2FFunctions%2F-1216412040) | [jvm]<br>@NotNull<br>open fun [getWorld](../-day-event/index.md#-2066259439%2FFunctions%2F-1216412040)(): @NotNullWorld |
| [isAsynchronous](../-day-event/index.md#-706610981%2FFunctions%2F-1216412040) | [jvm]<br>fun [isAsynchronous](../-day-event/index.md#-706610981%2FFunctions%2F-1216412040)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isCancelled](is-cancelled.md) | [jvm]<br>open override fun [isCancelled](is-cancelled.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setCancelled](set-cancelled.md) | [jvm]<br>open override fun [setCancelled](set-cancelled.md)(cancel: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [isCustom](is-custom.md) | [jvm]<br>val [isCustom](is-custom.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isEnd](is-end.md) | [jvm]<br>val [isEnd](is-end.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isNether](is-nether.md) | [jvm]<br>val [isNether](is-nether.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isOverworld](is-overworld.md) | [jvm]<br>val [isOverworld](is-overworld.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

## Inheritors

| Name |
|---|
| [NightEvent](../-night-event/index.md) |
| [DayEvent](../-day-event/index.md) |
