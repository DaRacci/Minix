---
title: me.racci.raccicore.api.events
---
//[RacciCore](../../index.html)/[me.racci.raccicore.api.events](index.html)



# Package me.racci.raccicore.api.events



## Types


| Name | Summary |
|---|---|
| [AbstractComboEvent](-abstract-combo-event/index.html) | [jvm]<br>sealed class [AbstractComboEvent](-abstract-combo-event/index.html) : [KPlayerEvent](-k-player-event/index.html) |
| [BlockData](-block-data/index.html) | [jvm]<br>data class [BlockData](-block-data/index.html)(block: Block, blockFace: BlockFace) |
| [DayEvent](-day-event/index.html) | [jvm]<br>class [DayEvent](-day-event/index.html)(world: World) : [KWorldEvent](-k-world-event/index.html)<br>This event is fired when the world turns to Day. |
| [KEvent](-k-event/index.html) | [jvm]<br>abstract class [KEvent](-k-event/index.html)(async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : Event, Cancellable<br>Represents an event. |
| [KPlayerEvent](-k-player-event/index.html) | [jvm]<br>abstract class [KPlayerEvent](-k-player-event/index.html)(player: Player, async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : PlayerEvent, Cancellable<br>Represents a player event. |
| [KWorldEvent](-k-world-event/index.html) | [jvm]<br>abstract class [KWorldEvent](-k-world-event/index.html)(world: World, async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : WorldEvent, Cancellable<br>Represents a World event, Includes variables [isOverworld](-k-world-event/is-nether.html) and [isEnd](-k-world-event/is-end.html) These make it easy to find what world type the event is within. |
| [NightEvent](-night-event/index.html) | [jvm]<br>class [NightEvent](-night-event/index.html)(world: World) : [KWorldEvent](-k-world-event/index.html)<br>This event is fired when the world turns to Night. |
| [PlayerDoubleOffhandEvent](-player-double-offhand-event/index.html) | [jvm]<br>class [PlayerDoubleOffhandEvent](-player-double-offhand-event/index.html)(player: Player, item: ItemStack?, offHandItem: ItemStack?, blockData: [BlockData](-block-data/index.html)?, entity: Entity?) : [AbstractComboEvent](-abstract-combo-event/index.html) |
| [PlayerEnterLiquidEvent](-player-enter-liquid-event/index.html) | [jvm]<br>class [PlayerEnterLiquidEvent](-player-enter-liquid-event/index.html)(player: Player, liquidType: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), from: Location, to: Location) : [KPlayerEvent](-k-player-event/index.html)<br>This event is fires when the player enters water, lava or a waterlogged block. |
| [PlayerExitLiquidEvent](-player-exit-liquid-event/index.html) | [jvm]<br>class [PlayerExitLiquidEvent](-player-exit-liquid-event/index.html)(player: Player, liquidType: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), from: Location, to: Location) : [KPlayerEvent](-k-player-event/index.html)<br>This event is fires when the player exits water, lava or a waterlogged block. |
| [PlayerLeftClickEvent](-player-left-click-event/index.html) | [jvm]<br>class [PlayerLeftClickEvent](-player-left-click-event/index.html)(player: Player, item: ItemStack?, blockData: [BlockData](-block-data/index.html)?, entity: Entity?) : [AbstractComboEvent](-abstract-combo-event/index.html) |
| [PlayerMoveFullXYZEvent](-player-move-full-x-y-z-event/index.html) | [jvm]<br>class [PlayerMoveFullXYZEvent](-player-move-full-x-y-z-event/index.html)(player: Player, from: Location, to: Location) : [KPlayerEvent](-k-player-event/index.html)<br>This event is fired when the player moves one full block, So if the player moves +1 in any direction this will apply. |
| [PlayerMoveXYZEvent](-player-move-x-y-z-event/index.html) | [jvm]<br>class [PlayerMoveXYZEvent](-player-move-x-y-z-event/index.html)(player: Player, from: Location, to: Location) : [KPlayerEvent](-k-player-event/index.html)<br>This event is fired only when the player moves, this means that unlike the normal PlayerMoveEvent it does not fire when the player looks around. |
| [PlayerOffhandEvent](-player-offhand-event/index.html) | [jvm]<br>class [PlayerOffhandEvent](-player-offhand-event/index.html)(player: Player, item: ItemStack?, offHandItem: ItemStack?, blockData: [BlockData](-block-data/index.html)?, entity: Entity?) : [AbstractComboEvent](-abstract-combo-event/index.html) |
| [PlayerRightClickEvent](-player-right-click-event/index.html) | [jvm]<br>class [PlayerRightClickEvent](-player-right-click-event/index.html)(player: Player, item: ItemStack?, blockData: [BlockData](-block-data/index.html)?, entity: Entity?) : [AbstractComboEvent](-abstract-combo-event/index.html) |
| [PlayerShiftDoubleOffhandEvent](-player-shift-double-offhand-event/index.html) | [jvm]<br>class [PlayerShiftDoubleOffhandEvent](-player-shift-double-offhand-event/index.html)(player: Player, item: ItemStack?, offHandItem: ItemStack?, blockData: [BlockData](-block-data/index.html)?, entity: Entity?) : [AbstractComboEvent](-abstract-combo-event/index.html) |
| [PlayerShiftLeftClickEvent](-player-shift-left-click-event/index.html) | [jvm]<br>class [PlayerShiftLeftClickEvent](-player-shift-left-click-event/index.html)(player: Player, item: ItemStack?, blockData: [BlockData](-block-data/index.html)?, entity: Entity?) : [AbstractComboEvent](-abstract-combo-event/index.html) |
| [PlayerShiftOffhandEvent](-player-shift-offhand-event/index.html) | [jvm]<br>class [PlayerShiftOffhandEvent](-player-shift-offhand-event/index.html)(player: Player, item: ItemStack?, offHandItem: ItemStack?, blockData: [BlockData](-block-data/index.html)?, entity: Entity?) : [AbstractComboEvent](-abstract-combo-event/index.html) |
| [PlayerShiftRightClickEvent](-player-shift-right-click-event/index.html) | [jvm]<br>class [PlayerShiftRightClickEvent](-player-shift-right-click-event/index.html)(player: Player, item: ItemStack?, blockData: [BlockData](-block-data/index.html)?, entity: Entity?) : [AbstractComboEvent](-abstract-combo-event/index.html) |
| [PlayerUnloadEvent](-player-unload-event/index.html) | [jvm]<br>class [PlayerUnloadEvent](-player-unload-event/index.html)(player: Player?, uuid: [UUID](https://docs.oracle.com/javase/8/docs/api/java/util/UUID.html)) : [KEvent](-k-event/index.html)<br>Called once it is safe to unload a Player's data. |

