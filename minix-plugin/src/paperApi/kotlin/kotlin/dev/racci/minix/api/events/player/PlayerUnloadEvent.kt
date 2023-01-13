package dev.racci.minix.api.events.player

import dev.racci.minix.api.data.MinixPlayer
import dev.racci.minix.api.events.CompanionEventHandler
import dev.racci.minix.api.events.MinixEvent
import org.bukkit.event.HandlerList

public actual class PlayerUnloadEvent actual constructor(public actual val minixPlayer: MinixPlayer) : MinixEvent(true) {
    public companion object : CompanionEventHandler() {
        @JvmStatic override fun getHandlerList(): HandlerList = super.getHandlerList()
    }
}
