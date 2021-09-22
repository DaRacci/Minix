@file:JvmName("KotlinEvent")
package me.racci.raccilib.events

abstract class KotlinEvent(async: Boolean = false): org.bukkit.event.Event(async), org.bukkit.event.Cancellable {

    private var cancelled = false

    override fun setCancelled(cancel: Boolean) {
        this.cancelled = true
    }

    override fun isCancelled(): Boolean {
        return cancelled
    }

    override fun getHandlers(): org.bukkit.event.HandlerList {
        return handlerList
    }

    companion object {
        private val handlerList = org.bukkit.event.HandlerList()

        @JvmStatic
        fun getHandlerList(): org.bukkit.event.HandlerList {
            return handlerList
        }
    }

}