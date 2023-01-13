package dev.racci.minix.api.extensions

import dev.racci.minix.data.structs.minecraft.BlockPos
import dev.racci.minix.data.structs.minecraft.ChunkPos
import dev.racci.minix.data.structs.minecraft.LocationPos
import dev.racci.minix.data.structs.minecraft.blockPosOf
import dev.racci.minix.data.structs.minecraft.locationPosOf
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block

public fun Block.asPos(): BlockPos = blockPosOf(x, y, z)
public fun Location.asBlockPos(): BlockPos = blockPosOf(blockX, blockY, blockZ)
public fun Location.asPos(): LocationPos = locationPosOf(x, y, z, yaw, pitch)
public fun Chunk.asPos(): ChunkPos = ChunkPos(x, z)

public fun BlockPos.asBukkitBlock(world: World): Block = world.getBlockAt(x.toInt(), y.toInt(), z.toInt())
public fun BlockPos.asBukkitLocation(world: World): Location = Location(world, x, y, z)

public fun LocationPos.asBukkitBlock(world: World): Block = world.getBlockAt(x.toInt(), y.toInt(), z.toInt())
public fun LocationPos.asBukkitLocation(world: World): Location = Location(world, x, y, z)
