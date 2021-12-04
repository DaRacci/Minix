//[RacciCore](../../../index.md)/[me.racci.raccicore.api.events](../index.md)/[PlayerUnloadEvent](index.md)

# PlayerUnloadEvent

[jvm]\
class [PlayerUnloadEvent](index.md)(player: Player?, uuid: [UUID](https://docs.oracle.com/javase/8/docs/api/java/util/UUID.html)) : [KEvent](../-k-event/index.md)

Called once it is safe to unload a Player's data.

**Fired Asynchronously**

#### Since

0.3.0

## Parameters

jvm

| | |
|---|---|
| player | The Player if able to be provided. |
| uuid | The UUID, this will always be provided. |

## Constructors

| | |
|---|---|
| [PlayerUnloadEvent](-player-unload-event.md) | [jvm]<br>fun [PlayerUnloadEvent](-player-unload-event.md)(player: Player? = null, uuid: [UUID](https://docs.oracle.com/javase/8/docs/api/java/util/UUID.html) = player?.uuid ?: throw IllegalArgumentException()) |

## Functions

| Name | Summary |
|---|---|
| [callEvent](../-day-event/index.md#-1071638799%2FFunctions%2F-1216412040) | [jvm]<br>open fun [callEvent](../-day-event/index.md#-1071638799%2FFunctions%2F-1216412040)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getEventName](../-day-event/index.md#1147460734%2FFunctions%2F-1216412040) | [jvm]<br>@NotNull<br>open fun [getEventName](../-day-event/index.md#1147460734%2FFunctions%2F-1216412040)(): @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getHandlers](../-k-event/get-handlers.md) | [jvm]<br>open override fun [getHandlers](../-k-event/get-handlers.md)(): HandlerList |
| [isAsynchronous](../-day-event/index.md#-706610981%2FFunctions%2F-1216412040) | [jvm]<br>fun [isAsynchronous](../-day-event/index.md#-706610981%2FFunctions%2F-1216412040)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isCancelled](../-k-event/is-cancelled.md) | [jvm]<br>open override fun [isCancelled](../-k-event/is-cancelled.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setCancelled](../-k-event/set-cancelled.md) | [jvm]<br>open override fun [setCancelled](../-k-event/set-cancelled.md)(cancel: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [player](player.md) | [jvm]<br>val [player](player.md): Player? = null |
| [uuid](uuid.md) | [jvm]<br>val [uuid](uuid.md): [UUID](https://docs.oracle.com/javase/8/docs/api/java/util/UUID.html) |
