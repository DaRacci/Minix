package dev.racci.minix.api.utils.minecraft.destructuring

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector

public operator fun Location.component1(): Double = x
public operator fun Location.component2(): Double = y
public operator fun Location.component3(): Double = z
public operator fun Location.component4(): World? = world

public operator fun Vector.component1(): Double = x
public operator fun Vector.component2(): Double = y
public operator fun Vector.component3(): Double = z
