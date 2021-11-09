package me.racci.raccicore.events

import org.bukkit.block.Block
import org.bukkit.block.BlockFace

interface IComboBlockEvent: IComboEvent {

    val block: Block

    val blockFace: BlockFace

}