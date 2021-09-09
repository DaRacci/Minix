@file:Suppress("unused")
@file:JvmName("KotlinEvent")
package me.racci.raccilib.events

import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

abstract class KotlinEvent: Event(), Cancellable {

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }


}