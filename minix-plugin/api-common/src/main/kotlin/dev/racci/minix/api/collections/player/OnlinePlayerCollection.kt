package dev.racci.minix.api.collections.player

import dev.racci.minix.api.callbacks.PlayerQuitCallback
import dev.racci.minix.api.data.MinixPlayer
import dev.racci.minix.api.events.player.WrappedPlayerQuitEvent
import dev.racci.minix.api.utils.kotlin.ifTrue
import dev.racci.minix.flowbus.receiver.EventReceiver
import dev.racci.minix.flowbus.subscribe
import org.apiguardian.api.API
import org.koin.core.component.KoinComponent

@API(status = API.Status.MAINTAINED, since = "5.0.0")
public interface OnlinePlayerCollection : MutableCollection<MinixPlayer>, EventReceiver, KoinComponent {

    public fun checkRegistration() {
        if (size == 1) {
            this.subscribe<WrappedPlayerQuitEvent> { quit(this.player) }
        } else if (size == 0) {
            this.unsubscribe()
        }
    }

    public fun add(
        player: MinixPlayer,
        onQuit: PlayerQuitCallback = PlayerQuitCallback.empty
    ): Boolean = this.add(player).ifTrue(::checkRegistration)

    public fun quit(player: MinixPlayer): Boolean = this.remove(player).ifTrue(::checkRegistration)

    public fun clearCallbacks() {
        this.toMutableList().forEach(::quit)
    }
}
