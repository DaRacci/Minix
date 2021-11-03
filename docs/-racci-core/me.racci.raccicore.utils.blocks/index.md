//[RacciCore](../../index.md)/[me.racci.raccicore.utils.blocks](index.md)

# Package me.racci.raccicore.utils.blocks

## Functions

| Name | Summary |
|---|---|
| [getOpposite](get-opposite.md) | [jvm]<br>fun [getOpposite](get-opposite.md)(face: BlockFace?): BlockFace<br>Returns the opposite BlockFace for a given BlockFace. E.g. EAST_NORTH_EAST will return WEST_SOUTH_WEST. SELF will return SELF. |
| [getPlacedAgainstFace](get-placed-against-face.md) | [jvm]<br>fun [getPlacedAgainstFace](get-placed-against-face.md)(existing: Block, newBlock: Block?): BlockFace<br>Gets the BlockFace of the existing block that must have been right-clicked to place the new Block |
| [getSupportingBlock](get-supporting-block.md) | [jvm]<br>fun [getSupportingBlock](get-supporting-block.md)(directional: Block): Block<br>Gets the block another block (e.g. a ladder) is attached to |
| [isLiquid](is-liquid.md) | [jvm]<br>fun [isLiquid](is-liquid.md)(block: Block): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
