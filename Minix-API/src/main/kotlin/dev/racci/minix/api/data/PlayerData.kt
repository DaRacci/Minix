@file:Suppress("UNUSED")

package dev.racci.minix.api.data

import dev.racci.minix.api.events.PlayerUnloadEvent
import dev.racci.minix.api.services.PlayerService
import java.util.UUID

data class PlayerData(
    val uuid: UUID,
) {

    var inAccess: Int = 0
        set(int) {
            field = int
            if (field == 0) {
                PlayerUnloadEvent(null, uuid).callEvent()
                PlayerService.remove(uuid)
            }
        }

    var lastOffhand: Long = 0
    var lastShift: Long = 0
    var lastLeftClick: Long = 0
    var lastRightClick: Long = 0
    var lastJump: Long = 0
}
