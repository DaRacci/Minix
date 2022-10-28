package dev.racci.minix.api.data

import dev.racci.minix.api.events.player.PlayerUnloadEvent
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.services.PlayerService
import dev.racci.minix.flowbus.FlowBus
import kotlinx.atomicfu.AtomicInt
import kotlinx.atomicfu.atomic
import org.apiguardian.api.API
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

// Workaround for https://youtrack.jetbrains.com/issue/KT-20427
@API(status = API.Status.INTERNAL, since = "5.0.0")
public abstract class PlatformPlayerData internal constructor() : KoinComponent {
    private val _accessCount: AtomicInt = atomic(0)
    protected open val wrappedRef: MinixPlayer by lazy { this.castOrThrow() }

    public var accessCount: Int
        get() = _accessCount.value
        set(int) {
            _accessCount.lazySet(int)
            if (int != 0) return

            get<FlowBus>().post(PlayerUnloadEvent(wrappedRef))
            get<PlayerService>().remove(wrappedRef.uuid)
        }

    public suspend fun withAccess(block: suspend (MinixPlayer) -> Unit) {
        _accessCount.incrementAndGet()
        block(wrappedRef)
        _accessCount.decrementAndGet()
    }
}
