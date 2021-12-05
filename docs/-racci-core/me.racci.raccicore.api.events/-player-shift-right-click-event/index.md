---
title: PlayerShiftRightClickEvent
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.events](../index.html)/[PlayerShiftRightClickEvent](index.html)



# PlayerShiftRightClickEvent



[jvm]\
class [PlayerShiftRightClickEvent](index.html)(player: Player, item: ItemStack?, blockData: [BlockData](../-block-data/index.html)?, entity: Entity?) : [AbstractComboEvent](../-abstract-combo-event/index.html)



## Functions


| Name | Summary |
|---|---|
| [callEvent](../-day-event/index.html#-1071638799%2FFunctions%2F863300109) | [jvm]<br>open fun [callEvent](../-day-event/index.html#-1071638799%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getEventName](../-day-event/index.html#1147460734%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>open fun [getEventName](../-day-event/index.html#1147460734%2FFunctions%2F863300109)(): @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getHandlers](../-k-player-event/get-handlers.html) | [jvm]<br>open override fun [getHandlers](../-k-player-event/get-handlers.html)(): HandlerList |
| [getPlayer](../-player-move-full-x-y-z-event/index.html#-1478213936%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>fun [getPlayer](../-player-move-full-x-y-z-event/index.html#-1478213936%2FFunctions%2F863300109)(): @NotNullPlayer |
| [isAsynchronous](../-day-event/index.html#-706610981%2FFunctions%2F863300109) | [jvm]<br>fun [isAsynchronous](../-day-event/index.html#-706610981%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isCancelled](../-k-player-event/is-cancelled.html) | [jvm]<br>open override fun [isCancelled](../-k-player-event/is-cancelled.html)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setCancelled](../-k-player-event/set-cancelled.html) | [jvm]<br>open override fun [setCancelled](../-k-player-event/set-cancelled.html)(cancel: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |


## Properties


| Name | Summary |
|---|---|
| [blockData](../-abstract-combo-event/block-data.html) | [jvm]<br>val [blockData](../-abstract-combo-event/block-data.html): [BlockData](../-block-data/index.html)? = null |
| [entity](../-abstract-combo-event/entity.html) | [jvm]<br>val [entity](../-abstract-combo-event/entity.html): Entity? = null |
| [hasItem](../-abstract-combo-event/has-item.html) | [jvm]<br>open val [hasItem](../-abstract-combo-event/has-item.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>If true this means the used item in the event is not a players fists. |
| [isBlockEvent](../-abstract-combo-event/is-block-event.html) | [jvm]<br>val [isBlockEvent](../-abstract-combo-event/is-block-event.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Returns if this event had a block involved. |
| [isEntityEvent](../-abstract-combo-event/is-entity-event.html) | [jvm]<br>val [isEntityEvent](../-abstract-combo-event/is-entity-event.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Returns if this event had an entity Involved. |
| [item](../-abstract-combo-event/item.html) | [jvm]<br>val [item](../-abstract-combo-event/item.html): ItemStack? |
| [player](../-player-move-full-x-y-z-event/index.html#-8709326%2FProperties%2F863300109) | [jvm]<br>val [player](../-player-move-full-x-y-z-event/index.html#-8709326%2FProperties%2F863300109): Player |

