---
title: PlayerUnloadEvent
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.events](../index.html)/[PlayerUnloadEvent](index.html)



# PlayerUnloadEvent



[jvm]\
class [PlayerUnloadEvent](index.html)(player: Player?, uuid: [UUID](https://docs.oracle.com/javase/8/docs/api/java/util/UUID.html)) : [KEvent](../-k-event/index.html)

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
| [PlayerUnloadEvent](-player-unload-event.html) | [jvm]<br>fun [PlayerUnloadEvent](-player-unload-event.html)(player: Player? = null, uuid: [UUID](https://docs.oracle.com/javase/8/docs/api/java/util/UUID.html) = player?.uuid ?: throw IllegalArgumentException()) |


## Functions


| Name | Summary |
|---|---|
| [callEvent](../-day-event/index.html#-1071638799%2FFunctions%2F863300109) | [jvm]<br>open fun [callEvent](../-day-event/index.html#-1071638799%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getEventName](../-day-event/index.html#1147460734%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>open fun [getEventName](../-day-event/index.html#1147460734%2FFunctions%2F863300109)(): @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getHandlers](../-k-event/get-handlers.html) | [jvm]<br>open override fun [getHandlers](../-k-event/get-handlers.html)(): HandlerList |
| [isAsynchronous](../-day-event/index.html#-706610981%2FFunctions%2F863300109) | [jvm]<br>fun [isAsynchronous](../-day-event/index.html#-706610981%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isCancelled](../-k-event/is-cancelled.html) | [jvm]<br>open override fun [isCancelled](../-k-event/is-cancelled.html)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setCancelled](../-k-event/set-cancelled.html) | [jvm]<br>open override fun [setCancelled](../-k-event/set-cancelled.html)(cancel: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |


## Properties


| Name | Summary |
|---|---|
| [player](player.html) | [jvm]<br>val [player](player.html): Player? = null |
| [uuid](uuid.html) | [jvm]<br>val [uuid](uuid.html): [UUID](https://docs.oracle.com/javase/8/docs/api/java/util/UUID.html) |

