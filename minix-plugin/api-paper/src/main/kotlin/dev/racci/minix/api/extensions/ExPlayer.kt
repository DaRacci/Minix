package dev.racci.minix.api.extensions

import dev.racci.minix.api.utils.minecraft.TameUtils
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

public infix fun Player.isOwner(entity: Entity): Boolean = TameUtils.isOwner(this, entity)

public val Player.isDay: Boolean get() = world.isDayTime
public val Player.isNight: Boolean get() = !isDay

public val Player.inOverworld: Boolean get() = world.isOverworld
public val Player.inNether: Boolean get() = world.isNether
public val Player.inEnd: Boolean get() = world.isEnd
