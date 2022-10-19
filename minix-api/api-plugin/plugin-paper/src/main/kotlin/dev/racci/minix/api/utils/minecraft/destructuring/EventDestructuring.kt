package dev.racci.minix.api.utils.minecraft.destructuring

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.entity.Vehicle
import org.bukkit.event.block.BlockEvent
import org.bukkit.event.entity.EntityEvent
import org.bukkit.event.entity.PlayerLeashEntityEvent
import org.bukkit.event.inventory.InventoryEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.inventory.InventoryPickupItemEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerExpChangeEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.server.TabCompleteEvent
import org.bukkit.event.vehicle.VehicleEvent
import org.bukkit.event.vehicle.VehicleMoveEvent
import org.bukkit.event.weather.WeatherEvent
import org.bukkit.event.world.WorldEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.ItemStack

operator fun BlockEvent.component1(): Block = block

operator fun EntityEvent.component1(): Entity = entity

operator fun InventoryEvent.component1(): Inventory = inventory
operator fun InventoryEvent.component2(): MutableList<HumanEntity> = viewers
operator fun InventoryEvent.component3(): InventoryView = view

operator fun InventoryMoveItemEvent.component1(): ItemStack = item
operator fun InventoryMoveItemEvent.component2(): Inventory = source
operator fun InventoryMoveItemEvent.component3(): Inventory = destination

operator fun InventoryPickupItemEvent.component1(): Item = item
operator fun InventoryPickupItemEvent.component2(): Inventory = inventory

operator fun PlayerEvent.component1() = player

operator fun PlayerLeashEntityEvent.component1(): Player = player
operator fun PlayerLeashEntityEvent.component2(): Entity = entity
operator fun PlayerLeashEntityEvent.component3(): Entity = leashHolder

operator fun TabCompleteEvent.component1(): CommandSender = sender
operator fun TabCompleteEvent.component2(): MutableList<String> = completions
operator fun TabCompleteEvent.component3(): String = buffer

operator fun VehicleEvent.component1(): Vehicle = vehicle

operator fun WeatherEvent.component1(): World = world

operator fun WorldEvent.component1(): World = world

operator fun VehicleMoveEvent.component2(): Location = from
operator fun VehicleMoveEvent.component3(): Location = to

operator fun PlayerExpChangeEvent.component2(): Int = amount

operator fun PlayerMoveEvent.component2(): Location = from
operator fun PlayerMoveEvent.component3(): Location = to

operator fun PlayerItemHeldEvent.component2(): Int = previousSlot
operator fun PlayerItemHeldEvent.component3(): Int = newSlot
