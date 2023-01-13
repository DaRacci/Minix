package dev.racci.minix.api.collections.player

import dev.racci.minix.api.callbacks.PlayerQuitCallback
import dev.racci.minix.api.data.MinixPlayer
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.api.utils.kotlin.ifNotNull
import dev.racci.minix.api.utils.kotlin.ifNull
import dev.racci.minix.api.utils.kotlin.ifTrue
import dev.racci.minix.flowbus.receiver.EventReceiver
import org.apiguardian.api.API

@API(status = API.Status.MAINTAINED, since = "5.0.0")
public class OnlinePlayerSet internal constructor(
    private val backingMap: MutableMap<MinixPlayer, PlayerQuitCallback> = mutableMapOf()
) : MutableSet<MinixPlayer> by backingMap.keys, OnlinePlayerCollection, EventReceiver by getKoin().get() {

    override fun add(
        player: MinixPlayer,
        onQuit: PlayerQuitCallback
    ): Boolean = super.add(player, onQuit).ifTrue {
        this.backingMap[player] = onQuit
    }

    override fun add(element: MinixPlayer): Boolean = this.backingMap
        .put(element, PlayerQuitCallback.empty).ifNull(::checkRegistration)

    override fun remove(element: MinixPlayer): Boolean = this.backingMap.remove(element)
        .ifNotNull { checkRegistration() }

    override fun removeAll(elements: Collection<MinixPlayer>): Boolean {
        return this.backingMap.keys.removeAll(elements.toSet()).also { this.checkRegistration() }
    }

    override fun retainAll(elements: Collection<MinixPlayer>): Boolean {
        return this.backingMap.keys.retainAll(elements.toSet()).also { this.checkRegistration() }
    }

    override fun quit(player: MinixPlayer): Boolean = super.quit(player).ifTrue {
        this.backingMap.remove(player)?.invoke(player)
    }

    override fun addAll(elements: Collection<MinixPlayer>): Boolean = this.backingMap
        .putAll(elements.associateWith { PlayerQuitCallback.empty })
        .ifNull(::checkRegistration)

    override fun clear() {
        this.clearCallbacks()
    }
}
