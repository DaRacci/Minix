//[RacciCore](../../../index.md)/[me.racci.raccicore.api.utils.minecraft](../index.md)/[BlockUtils](index.md)

# BlockUtils

[jvm]\
object [BlockUtils](index.md)

Utilities for Blocks

## Functions

| Name | Summary |
|---|---|
| [getOpposite](get-opposite.md) | [jvm]<br>fun [getOpposite](get-opposite.md)(face: BlockFace): BlockFace<br>Returns the opposite BlockFace for a given BlockFace. E.g. EAST_NORTH_EAST will return WEST_SOUTH_WEST. SELF will return SELF. |
| [getPlacedAgainstFace](get-placed-against-face.md) | [jvm]<br>fun [getPlacedAgainstFace](get-placed-against-face.md)(existing: Block, newBlock: Block?): BlockFace<br>Gets the BlockFace of the existing block that must have been right-clicked to place the new Block |
| [getSupportingBlock](get-supporting-block.md) | [jvm]<br>fun [getSupportingBlock](get-supporting-block.md)(directional: Block): Block<br>Gets the block another block (e.g. a ladder) is attached to |
| [getVein](get-vein.md) | [jvm]<br>fun [getVein](get-vein.md)(start: Block, materials: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Material&gt;, limit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [ArrayList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)&lt;Block&gt;<br>Gets an array list of all blocks in contact with each other. |
| [isLiquid](is-liquid.md) | [jvm]<br>fun [isLiquid](is-liquid.md)(block: Block): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns the value of if the block is a liquid 0 = Not a liquid 1 = Water or waterlogged 2 = Lava |
