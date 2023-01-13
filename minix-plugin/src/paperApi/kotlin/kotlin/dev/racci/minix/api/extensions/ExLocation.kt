package dev.racci.minix.api.extensions

import org.bukkit.Effect
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.TreeType
import org.bukkit.entity.Arrow
import org.bukkit.entity.Item
import org.bukkit.entity.LightningStrike
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

public fun Location.dropItem(item: ItemStack): Item = world.dropItem(this, item)
public fun Location.dropItemNaturally(item: ItemStack): Item = world.dropItemNaturally(this, item)

public fun Location.spawnArrow(
    direction: Vector,
    speed: Float,
    spread: Float
): Arrow = world.spawnArrow(this, direction, speed, spread)

public fun Location.generateTree(type: TreeType): Boolean = world.generateTree(this, type)

public fun Location.strikeLightning(): LightningStrike = world.strikeLightning(this)
public fun Location.strikeLightningEffect(): LightningStrike = world.strikeLightningEffect(this)

public fun Location.playEffect(
    effect: Effect,
    data: Int
): Unit = world.playEffect(this, effect, data)

public fun Location.playEffect(
    effect: Effect,
    data: Int,
    radius: Int
): Unit = world.playEffect(this, effect, data, radius)

public fun <T> Location.playEffect(
    effect: Effect,
    data: T
): Unit = world.playEffect(this, effect, data)

public fun <T> Location.playEffect(
    effect: Effect,
    data: T,
    radius: Int
): Unit = world.playEffect(this, effect, data, radius)

public fun Location.playSound(
    sound: Sound,
    volume: Float,
    pitch: Float
): Unit = world.playSound(this, sound, volume, pitch)

public val Location.isDay: Boolean get() = world.isDayTime
public val Location.isNight: Boolean get() = !isDay
