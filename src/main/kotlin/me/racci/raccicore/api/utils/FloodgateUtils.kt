package me.racci.raccicore.api.utils

import me.racci.raccicore.core.managers.HookManager
import org.bukkit.entity.Player
import java.util.*

val Player.uuid: UUID
    get() {
        return HookManager.floodgateCache.computeIfAbsent(this.uniqueId) {
            if(HookManager.floodgate?.isFloodgatePlayer(uniqueId) == true) {
                HookManager.floodgate!!.getPlayer(uniqueId).correctUniqueId
            } else uniqueId
        }
    }