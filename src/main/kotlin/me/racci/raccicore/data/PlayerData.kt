package me.racci.raccicore.data

internal data class PlayerData(
    val player: org.bukkit.entity.Player
    ) {

    fun init() {
        PlayerManager.addPlayerData(this)
    }

    var lastOffhand: Long = 0
    var lastShift: Long = 0
    var lastLeftClick: Long = 0
    var lastRightClick: Long = 0
    var lastJump: Long = 0

}
