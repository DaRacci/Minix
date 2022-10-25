package dev.racci.minix.api.collections.player

import dev.racci.minix.api.callbacks.PlayerQuitCallback
import dev.racci.minix.api.data.MinixPlayer
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.api.utils.kotlin.ifTrue
import dev.racci.minix.flowbus.receiver.EventReceiver
import org.apiguardian.api.API
import java.util.LinkedList

@API(status = API.Status.MAINTAINED, since = "5.0.0")
public class OnlinePlayerList internal constructor(
    players: Array<out Pair<MinixPlayer, PlayerQuitCallback>>
) : LinkedList<MinixPlayer>(), OnlinePlayerCollection, EventReceiver by getKoin().get() {
    private val quitCallbacks = mutableMapOf(*players)

    override fun add(
        player: MinixPlayer,
        onQuit: PlayerQuitCallback
    ): Boolean = super<OnlinePlayerCollection>.add(player, onQuit).ifTrue {
        quitCallbacks[player] = onQuit
    }

    override fun add(element: MinixPlayer): Boolean = super<LinkedList>
        .add(element)
        .ifTrue(::checkRegistration)

    override fun remove(element: MinixPlayer): Boolean = super.remove(element)
        .ifTrue(::checkRegistration)

    override fun addFirst(e: MinixPlayer?) {
        super.addFirst(e)
        this.checkRegistration()
    }

    override fun addLast(e: MinixPlayer?) {
        super.addLast(e)
        this.checkRegistration()
    }

    override fun addAll(index: Int, elements: Collection<MinixPlayer>): Boolean {
        return super.addAll(index, elements).also { this.checkRegistration() }
    }

    override fun add(index: Int, element: MinixPlayer) {
        super<LinkedList>.add(index, element)
        this.checkRegistration()
    }

    override fun removeFirst(): MinixPlayer {
        return super.removeFirst().also { this.checkRegistration() }
    }

    override fun removeLast(): MinixPlayer {
        return super.removeLast().also { this.checkRegistration() }
    }

    override fun removeFirstOccurrence(o: Any?): Boolean {
        return super.removeFirstOccurrence(o).also { this.checkRegistration() }
    }

    override fun removeLastOccurrence(o: Any?): Boolean {
        return super.removeLastOccurrence(o).also { this.checkRegistration() }
    }

    override fun removeAt(index: Int): MinixPlayer {
        return super.removeAt(index).also { this.checkRegistration() }
    }

    override fun offer(e: MinixPlayer?): Boolean {
        return super.offer(e).also { this.checkRegistration() }
    }

    override fun offerFirst(e: MinixPlayer?): Boolean {
        return super.offerFirst(e).also { this.checkRegistration() }
    }

    override fun offerLast(e: MinixPlayer?): Boolean {
        return super.offerLast(e).also { this.checkRegistration() }
    }

    override fun poll(): MinixPlayer? {
        return super.poll().also { this.checkRegistration() }
    }

    override fun pollFirst(): MinixPlayer? {
        return super.pollFirst().also { this.checkRegistration() }
    }

    override fun pollLast(): MinixPlayer? {
        return super.pollLast().also { this.checkRegistration() }
    }

    override fun quit(player: MinixPlayer): Boolean = super.quit(player).ifTrue {
        quitCallbacks.remove(player)?.invoke(player)
    }
}
