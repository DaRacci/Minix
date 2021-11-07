package me.racci.raccicore.utils.extensions

import org.bukkit.Effect
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.TreeType
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

fun Location.dropItem(item: ItemStack) = world.dropItem(this, item)
fun Location.dropItemNaturally(item: ItemStack) = world.dropItemNaturally(this, item)

fun Location.spawnArrow(direction: Vector, speed: Float, spread: Float) = world.spawnArrow(this, direction, speed, spread)

fun Location.generateTree(type: TreeType) = world.generateTree(this, type)

fun Location.strikeLightning() = world.strikeLightning(this)
fun Location.strikeLightningEffect() = world.strikeLightningEffect(this)

fun Location.playEffect(effect: Effect, data: Int) = world.playEffect(this, effect, data)
fun Location.playEffect(effect: Effect, data: Int, radius: Int) = world.playEffect(this, effect, data, radius)
fun <T> Location.playEffect(effect: Effect, data: T) = world.playEffect(this, effect, data)
fun <T> Location.playEffect(effect: Effect, data: T, radius: Int) = world.playEffect(this, effect, data, radius)

fun Location.playSound(sound: Sound, volume: Float, pitch: Float) = world.playSound(this, sound, volume, pitch)