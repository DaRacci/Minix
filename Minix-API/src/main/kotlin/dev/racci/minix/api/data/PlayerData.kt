package dev.racci.minix.api.data

import dev.racci.minix.api.events.LiquidType
import dev.racci.minix.api.events.PlayerUnloadEvent
import dev.racci.minix.api.services.PlayerService
import dev.racci.minix.api.utils.now
import kotlinx.atomicfu.AtomicInt
import kotlinx.atomicfu.atomic
import kotlinx.datetime.Instant
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds

data class PlayerData(val uuid: UUID) {

    private var atomicAccess: AtomicInt = atomic(0)
    var inAccess: Int
        get() = atomicAccess.value
        set(int) {
            atomicAccess.lazySet(int)
            if (atomicAccess.value == 0) {
                PlayerUnloadEvent(null, uuid).callEvent()
                PlayerService.remove(uuid)
            }
        }

    private val instantCache = arrayOfNulls<Instant>(5)

    private fun doubleOrAdd(index: Int): Boolean {
        val now = now()
        val instant = instantCache[index]

        when {
            instant == null || now - instant > DOUBLE_DELAY -> {
                instantCache[index] = now
            }
            else -> instantCache[index] = null
        }
        return instantCache[index] == null
    }

    val isDoubleOffhand: Boolean get() = doubleOrAdd(0)
    val isDoubleSneak: Boolean get() = doubleOrAdd(1)
    val isDoubleJump: Boolean get() = doubleOrAdd(2)
    val isDoubleAttack: Boolean get() = doubleOrAdd(3)
    val isDoubleInteract: Boolean get() = doubleOrAdd(4)

    var liquidType: LiquidType = LiquidType.NON

    companion object {
        val DOUBLE_DELAY = 500.milliseconds
    }
}
