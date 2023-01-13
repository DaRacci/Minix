package dev.racci.minix.api.extensions

import org.bukkit.World

public val World.isNight: Boolean get() = !isDayTime

public val World.isOverworld: Boolean get() = environment == World.Environment.NORMAL

public val World.isNether: Boolean get() = environment == World.Environment.NETHER

public val World.isEnd: Boolean get() = environment == World.Environment.THE_END
