package dev.racci.minix.api.extensions

import org.bukkit.World
import org.bukkit.event.world.WorldEvent

/** @returns If this event took place in the overworld. */
public val WorldEvent.isOverworld: Boolean get() = world.environment == World.Environment.NORMAL

/** @returns If this event took place in the nether. */
public val WorldEvent.isNether: Boolean get() = world.environment == World.Environment.NETHER

/** @returns If this event took place in the end. */
public val WorldEvent.isEnd: Boolean get() = world.environment == World.Environment.THE_END

/** @returns If this event took place in a custom world. */
public val WorldEvent.isCustom: Boolean get() = world.environment == World.Environment.CUSTOM
