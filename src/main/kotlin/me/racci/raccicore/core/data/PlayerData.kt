@file:Suppress("UNUSED")
package me.racci.raccicore.core.data

import me.racci.raccicore.api.events.PlayerUnloadEvent
import me.racci.raccicore.core.managers.PlayerManager
import java.util.*

data class PlayerData(
    val uuid: UUID
) {

    var inAccess: Int = 0
        set(int) {
            field = int
            if(field == 0) {
                PlayerUnloadEvent(null, (uuid)).callEvent()
                PlayerManager.remove(uuid)
            }
        }

    var lastOffhand: Long = 0
    var lastShift: Long = 0
    var lastLeftClick: Long = 0
    var lastRightClick: Long = 0
    var lastJump: Long = 0
}