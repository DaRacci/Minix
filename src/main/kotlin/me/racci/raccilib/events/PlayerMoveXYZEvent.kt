@file:Suppress("unused")
@file:JvmName("PlayerMoveXYZEvent")
package me.racci.raccilib.events

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerMoveXYZEvent(
    val player: Player, val from: Location, var to: Location
) : KotlinEvent() {

    private var isCancelled = false
    override fun isCancelled(): Boolean {
        return isCancelled
    }
    override fun setCancelled(isCancelled: Boolean) {
        this.isCancelled = isCancelled
    }

}

class PlayerMoveFullXYZEvent(
    val player: Player, val from: Location, var to: Location
) : KotlinEvent() {

    private var isCancelled = false

    override fun isCancelled(): Boolean {
        return isCancelled
    }
    override fun setCancelled(isCancelled: Boolean) {
        this.isCancelled = isCancelled
    }
}