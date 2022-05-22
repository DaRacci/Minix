package dev.racci.minix.api.extensions

import dev.racci.minix.api.utils.minecraft.TameUtils
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

infix fun Player.isOwner(entity: Entity) = TameUtils.isOwner(this, entity)
