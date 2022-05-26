package dev.racci.minix.api.extensions

import dev.racci.minix.api.utils.minecraft.TameUtils
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

infix fun Player.isOwner(entity: Entity) = TameUtils.isOwner(this, entity)

val Player.isDay: Boolean get() = world.isDayTime
val Player.isNight: Boolean get() = !isDay

val Player.inOverworld get() = world.isOverworld
val Player.inNether get() = world.isNether
val Player.inEnd get() = world.isEnd
