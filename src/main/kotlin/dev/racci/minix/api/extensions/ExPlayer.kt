package dev.racci.minix.api.extensions

import dev.racci.minix.api.utils.minecraft.TameUtils
import dev.racci.minix.api.utils.minecraft.WorldUtils
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

infix fun Player.isOwner(entity: Entity) = TameUtils.isOwner(this, entity)

fun Player.isDay() = WorldUtils.isDay(this)

fun Player.isNight() = WorldUtils.isNight(this)

fun Player.inOverworld() = WorldUtils.isOverworld(this.world)

fun Player.inNether() = WorldUtils.isNether(this.world)

fun Player.inEnd() = WorldUtils.isEnd(this.world)
