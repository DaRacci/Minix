---
title: sendBlockChange
---
//[RacciCore](../../index.html)/[me.racci.raccicore.api.extensions](index.html)/[sendBlockChange](send-block-change.html)



# sendBlockChange



[jvm]\
fun Block.[sendBlockChange](send-block-change.html)(material: Material, data: [Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html) = 0, vararg players: Player)



Send a block change to the [players](send-block-change.html) This block change is only visual and has no actual effect on the block.



## Parameters


jvm

| | |
|---|---|
| material | The new Material. |
| data | Extra data to apply to the Block. |
| players | The players to see this change. |





[jvm]\
fun Block.[sendBlockChange](send-block-change.html)(blockData: BlockData, vararg players: Player)



Send a block change to the [players](send-block-change.html) This block change is only visual and has no actual effect on the block.



## Parameters


jvm

| | |
|---|---|
| blockData | The new BlockData. |
| players | The players to see this change. |




