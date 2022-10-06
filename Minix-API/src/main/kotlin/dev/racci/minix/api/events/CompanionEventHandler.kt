package dev.racci.minix.api.events

import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.utils.kotlin.companionParent
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import kotlin.reflect.full.isSubclassOf

/**
 * Allows for an easy way to handle those pesky HandlerLists in Bukkit.
 *
 * By extending this event you'll gain the wonderful handlerMap I've created.
 * All that's left for you will be overriding the getHandlerList() method, marking it as @JvmStatic, and calling super.
 *
 * ```
 * companion object : CompanionEventHandler() {
 *      @JvmStatic
 *      override fun getHandlerList() = super.getHandlerList()
 * }
 * ```
 *
 * @throws IllegalStateException If this class is not a companion object of an event.
 */
open class CompanionEventHandler {

    init {
        if (!this::class.isCompanion) error("CompanionEventHandler must be a companion object.")
        if (!this::class.companionParent!!.isSubclassOf(Event::class)) error("CompanionEventHandler must be a companion object of an event.")
    }

    open fun getHandlerList(): HandlerList = KEvent.handlerMap[this::class.companionParent.castOrThrow()]
}
