package me.racci.raccicore.events

abstract class KotlinEvent(async: Boolean = false): org.bukkit.event.Event(async), org.bukkit.event.Cancellable {

    private var cancelled = false
    override fun isCancelled() = cancelled
    override fun setCancelled(cancel: Boolean) {cancelled = cancel}

    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = org.bukkit.event.HandlerList()

        @JvmStatic
        fun getHandlerList() = handlerList
    }
}

abstract class KPlayerEvent(player: org.bukkit.entity.Player, async: Boolean = false): org.bukkit.event.player.PlayerEvent(player, async), org.bukkit.event.Cancellable {

    private var cancelled = false
    override fun isCancelled() = cancelled
    override fun setCancelled(cancel: Boolean) {cancelled = cancel}

    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = org.bukkit.event.HandlerList()

        @JvmStatic
        fun getHandlerList() = handlerList
    }
}

