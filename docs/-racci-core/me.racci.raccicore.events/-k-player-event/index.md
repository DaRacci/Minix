//[RacciCore](../../../index.md)/[me.racci.raccicore.events](../index.md)/[KPlayerEvent](index.md)

# KPlayerEvent

[jvm]\
abstract class [KPlayerEvent](index.md)(player: Player, async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : PlayerEvent, Cancellable

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
| [getPlayer](index.md#-1478213936%2FFunctions%2F-1216412040) | [jvm]<br>@NotNull<br>fun [getPlayer](index.md#-1478213936%2FFunctions%2F-1216412040)(): @NotNullPlayer |
| [isAsynchronous](../-day-event/index.md#-706610981%2FFunctions%2F-1216412040) | [jvm]<br>fun [isAsynchronous](../-day-event/index.md#-706610981%2FFunctions%2F-1216412040)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isCancelled](is-cancelled.md) | [jvm]<br>open override fun [isCancelled](is-cancelled.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setCancelled](set-cancelled.md) | [jvm]<br>open override fun [setCancelled](set-cancelled.md)(cancel: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [player](index.md#-8709326%2FProperties%2F-1216412040) | [jvm]<br>val [player](index.md#-8709326%2FProperties%2F-1216412040): Player |
