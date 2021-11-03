//[RacciCore](../../../index.md)/[me.racci.raccicore.events](../index.md)/[PlayerDoubleOffhandEvent](index.md)

# PlayerDoubleOffhandEvent

[jvm]\
class [PlayerDoubleOffhandEvent](index.md)(player: Player, mainItem: ItemStack?, offhandItem: ItemStack?) : [KotlinEvent](../-kotlin-event/index.md)

Player double offhand event

## Constructors

| | |
|---|---|
| [PlayerDoubleOffhandEvent](-player-double-offhand-event.md) | [jvm]<br>fun [PlayerDoubleOffhandEvent](-player-double-offhand-event.md)(player: Player, mainItem: ItemStack?, offhandItem: ItemStack?)<br>Create empty Player double offhand event |

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
| [hasMain](has-main.md) | [jvm]<br>val [hasMain](has-main.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Has main |
| [hasOffhand](has-offhand.md) | [jvm]<br>val [hasOffhand](has-offhand.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Has offhand |
| [mainItem](main-item.md) | [jvm]<br>val [mainItem](main-item.md): ItemStack? |
| [offhandItem](offhand-item.md) | [jvm]<br>val [offhandItem](offhand-item.md): ItemStack? |
| [player](player.md) | [jvm]<br>val [player](player.md): Player |
