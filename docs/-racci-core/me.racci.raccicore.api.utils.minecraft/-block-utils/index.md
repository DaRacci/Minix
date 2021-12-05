---
title: BlockUtils
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.minecraft](../index.html)/[BlockUtils](index.html)



# BlockUtils



[jvm]\
object [BlockUtils](index.html)

Utilities for Blocks



## Functions


| Name | Summary |
|---|---|
| [getOpposite](get-opposite.html) | [jvm]<br>fun [getOpposite](get-opposite.html)(face: BlockFace): BlockFace<br>Returns the opposite BlockFace for a given BlockFace. E.g. EAST_NORTH_EAST will return WEST_SOUTH_WEST. SELF will return SELF. |
| [getPlacedAgainstFace](get-placed-against-face.html) | [jvm]<br>fun [getPlacedAgainstFace](get-placed-against-face.html)(existing: Block, newBlock: Block?): BlockFace<br>Gets the BlockFace of the existing block that must have been right-clicked to place the new Block |
| [getSupportingBlock](get-supporting-block.html) | [jvm]<br>fun [getSupportingBlock](get-supporting-block.html)(directional: Block): Block<br>Gets the block another block (e.g. a ladder) is attached to |
| [getVein](get-vein.html) | [jvm]<br>fun [getVein](get-vein.html)(start: Block, materials: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Material&gt;, limit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [ArrayList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)&lt;Block&gt;<br>Gets an array list of all blocks in contact with each other. |
| [isLiquid](is-liquid.html) | [jvm]<br>fun [isLiquid](is-liquid.html)(block: Block): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns the value of if the block is a liquid 0 = Not a liquid 1 = Water or waterlogged 2 = Lava |

