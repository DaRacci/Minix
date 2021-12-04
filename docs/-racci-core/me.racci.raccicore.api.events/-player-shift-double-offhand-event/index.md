//[RacciCore](../../../index.md)/[me.racci.raccicore.api.events](../index.md)/[PlayerShiftDoubleOffhandEvent](index.md)

# PlayerShiftDoubleOffhandEvent

[jvm]\
class [PlayerShiftDoubleOffhandEvent](index.md)(player: Player, item: ItemStack?, offHandItem: ItemStack?, blockData: [BlockData](../-block-data/index.md)?, entity: Entity?) : [AbstractComboEvent](../-abstract-combo-event/index.md)

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
| [blockData](../-abstract-combo-event/block-data.md) | [jvm]<br>val [blockData](../-abstract-combo-event/block-data.md): [BlockData](../-block-data/index.md)? = null |
| [entity](../-abstract-combo-event/entity.md) | [jvm]<br>val [entity](../-abstract-combo-event/entity.md): Entity? = null |
| [hasItem](has-item.md) | [jvm]<br>open override val [hasItem](has-item.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>If true this means that either one hand or both hands have an item and are not null. |
| [isBlockEvent](../-abstract-combo-event/is-block-event.md) | [jvm]<br>val [isBlockEvent](../-abstract-combo-event/is-block-event.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Returns if this event had a block involved. |
| [isEntityEvent](../-abstract-combo-event/is-entity-event.md) | [jvm]<br>val [isEntityEvent](../-abstract-combo-event/is-entity-event.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Returns if this event had an entity Involved. |
| [item](../-abstract-combo-event/item.md) | [jvm]<br>val [item](../-abstract-combo-event/item.md): ItemStack? |
| [offHandItem](off-hand-item.md) | [jvm]<br>val [offHandItem](off-hand-item.md): ItemStack? |
| [player](../-player-move-full-x-y-z-event/index.md#-8709326%2FProperties%2F-1216412040) | [jvm]<br>val [player](../-player-move-full-x-y-z-event/index.md#-8709326%2FProperties%2F-1216412040): Player |
