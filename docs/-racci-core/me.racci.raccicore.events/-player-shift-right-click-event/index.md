//[RacciCore](../../../index.md)/[me.racci.raccicore.events](../index.md)/[PlayerShiftRightClickEvent](index.md)

# PlayerShiftRightClickEvent

[jvm]\
class [PlayerShiftRightClickEvent](index.md)(player: Player, usedItem: ItemStack?, clickedLocation: Location?, clickedBlock: Block?, clickedBlockFace: BlockFace?, clickedEntity: Entity?) : [KotlinEvent](../-kotlin-event/index.md)

Player shift right click event

## Constructors

| | |
|---|---|
| [PlayerShiftRightClickEvent](-player-shift-right-click-event.md) | [jvm]<br>fun [PlayerShiftRightClickEvent](-player-shift-right-click-event.md)(player: Player, usedItem: ItemStack?, clickedLocation: Location?, clickedBlock: Block?, clickedBlockFace: BlockFace?, clickedEntity: Entity?)<br>Create empty Player shift right click event |

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
| [clickedBlock](clicked-block.md) | [jvm]<br>val [clickedBlock](clicked-block.md): Block? |
| [clickedBlockFace](clicked-block-face.md) | [jvm]<br>val [clickedBlockFace](clicked-block-face.md): BlockFace? |
| [clickedEntity](clicked-entity.md) | [jvm]<br>val [clickedEntity](clicked-entity.md): Entity? |
| [clickedLocation](clicked-location.md) | [jvm]<br>val [clickedLocation](clicked-location.md): Location? |
| [hasBlock](has-block.md) | [jvm]<br>val [hasBlock](has-block.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Has block |
| [hasItem](has-item.md) | [jvm]<br>val [hasItem](has-item.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Has item |
| [player](player.md) | [jvm]<br>val [player](player.md): Player |
| [usedItem](used-item.md) | [jvm]<br>val [usedItem](used-item.md): ItemStack? |
