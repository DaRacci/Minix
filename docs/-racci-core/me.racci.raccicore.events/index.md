//[RacciCore](../../index.md)/[me.racci.raccicore.events](index.md)

# Package me.racci.raccicore.events

## Types

| Name | Summary |
|---|---|
| [DayEvent](-day-event/index.md) | [jvm]<br>class [DayEvent](-day-event/index.md)(world: World) : [KotlinEvent](-kotlin-event/index.md) |
| [KotlinEvent](-kotlin-event/index.md) | [jvm]<br>abstract class [KotlinEvent](-kotlin-event/index.md)(async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : Event, Cancellable |
| [KPlayerEvent](-k-player-event/index.md) | [jvm]<br>abstract class [KPlayerEvent](-k-player-event/index.md)(player: Player, async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : PlayerEvent, Cancellable |
| [NightEvent](-night-event/index.md) | [jvm]<br>class [NightEvent](-night-event/index.md)(world: World) : [KotlinEvent](-kotlin-event/index.md) |
| [PlayerDoubleOffhandEvent](-player-double-offhand-event/index.md) | [jvm]<br>class [PlayerDoubleOffhandEvent](-player-double-offhand-event/index.md)(player: Player, mainItem: ItemStack?, offhandItem: ItemStack?) : [KotlinEvent](-kotlin-event/index.md)<br>Player double offhand event |
| [PlayerEnterLiquidEvent](-player-enter-liquid-event/index.md) | [jvm]<br>class [PlayerEnterLiquidEvent](-player-enter-liquid-event/index.md)(player: Player, liquidType: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), from: Location, to: Location) : [KotlinEvent](-kotlin-event/index.md)<br>Player enter liquid event |
| [PlayerExitLiquidEvent](-player-exit-liquid-event/index.md) | [jvm]<br>class [PlayerExitLiquidEvent](-player-exit-liquid-event/index.md)(player: Player, liquidType: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), from: Location, to: Location) : [KotlinEvent](-kotlin-event/index.md)<br>Player exit liquid event |
| [PlayerLeftClickEvent](-player-left-click-event/index.md) | [jvm]<br>class [PlayerLeftClickEvent](-player-left-click-event/index.md)(player: Player, usedItem: ItemStack?, clickedLocation: Location?, clickedBlock: Block?, clickedBlockFace: BlockFace?, clickedEntity: Entity?) : [KotlinEvent](-kotlin-event/index.md)<br>Player left click event |
| [PlayerMoveFullXYZEvent](-player-move-full-x-y-z-event/index.md) | [jvm]<br>class [PlayerMoveFullXYZEvent](-player-move-full-x-y-z-event/index.md)(player: Player, from: Location, to: Location) : [KotlinEvent](-kotlin-event/index.md)<br>Player move full x y z event |
| [PlayerMoveXYZEvent](-player-move-x-y-z-event/index.md) | [jvm]<br>class [PlayerMoveXYZEvent](-player-move-x-y-z-event/index.md)(player: Player, from: Location, to: Location) : [KotlinEvent](-kotlin-event/index.md)<br>Player move x y z event |
| [PlayerOffhandEvent](-player-offhand-event/index.md) | [jvm]<br>class [PlayerOffhandEvent](-player-offhand-event/index.md)(player: Player, mainItem: ItemStack?, offhandItem: ItemStack?) : [KotlinEvent](-kotlin-event/index.md)<br>Player offhand event |
| [PlayerRightClickEvent](-player-right-click-event/index.md) | [jvm]<br>class [PlayerRightClickEvent](-player-right-click-event/index.md)(player: Player, usedItem: ItemStack?, clickedLocation: Location?, clickedBlock: Block?, clickedBlockFace: BlockFace?, clickedEntity: Entity?) : [KotlinEvent](-kotlin-event/index.md)<br>Player right click event |
| [PlayerShiftDoubleOffhandEvent](-player-shift-double-offhand-event/index.md) | [jvm]<br>class [PlayerShiftDoubleOffhandEvent](-player-shift-double-offhand-event/index.md)(player: Player, mainItem: ItemStack?, offhandItem: ItemStack?) : [KotlinEvent](-kotlin-event/index.md)<br>Player shift double offhand event |
| [PlayerShiftLeftClickEvent](-player-shift-left-click-event/index.md) | [jvm]<br>class [PlayerShiftLeftClickEvent](-player-shift-left-click-event/index.md)(player: Player, usedItem: ItemStack?, clickedLocation: Location?, clickedBlock: Block?, clickedBlockFace: BlockFace?, clickedEntity: Entity?) : [KotlinEvent](-kotlin-event/index.md)<br>Player shift left click event |
| [PlayerShiftOffhandEvent](-player-shift-offhand-event/index.md) | [jvm]<br>class [PlayerShiftOffhandEvent](-player-shift-offhand-event/index.md)(player: Player, mainItem: ItemStack?, offhandItem: ItemStack?) : [KotlinEvent](-kotlin-event/index.md)<br>Player shift offhand event |
| [PlayerShiftRightClickEvent](-player-shift-right-click-event/index.md) | [jvm]<br>class [PlayerShiftRightClickEvent](-player-shift-right-click-event/index.md)(player: Player, usedItem: ItemStack?, clickedLocation: Location?, clickedBlock: Block?, clickedBlockFace: BlockFace?, clickedEntity: Entity?) : [KotlinEvent](-kotlin-event/index.md)<br>Player shift right click event |
