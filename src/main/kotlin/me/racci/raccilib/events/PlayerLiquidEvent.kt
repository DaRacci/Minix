@file:Suppress("unused")
@file:JvmName("PlayerLiquidEvent")
package me.racci.raccilib.events

import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerEnterLiquidEvent(
    val player: Player, val liquidType: Int, val from: Block, val to: Block
) : Event(), Cancellable {
    override fun getHandlers(): HandlerList {
        return handlerList
    }
    private var isCancelled = false
    override fun isCancelled(): Boolean {
        return isCancelled
    }
    override fun setCancelled(isCancelled: Boolean) {
        this.isCancelled = isCancelled
    }
    companion object {
        val handlerList = HandlerList()
    }
}

class PlayerExitLiquidEvent(
    val player: Player, val liquidType: Int, val from: Block, val to: Block
) : Event(), Cancellable {
    override fun getHandlers(): HandlerList {
        return handlerList
    }
    private var isCancelled = false
    override fun isCancelled(): Boolean {
        return isCancelled
    }
    override fun setCancelled(isCancelled: Boolean) {
        this.isCancelled = isCancelled
    }
    companion object {
        val handlerList = HandlerList()
    }
}