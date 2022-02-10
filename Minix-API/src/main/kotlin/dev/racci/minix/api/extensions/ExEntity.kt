package dev.racci.minix.api.extensions

import dev.racci.minix.api.utils.minecraft.TameUtils
import org.bukkit.entity.Entity

fun Entity.isTamed() = TameUtils.isTamed(this)

fun Entity.hasOwner() = TameUtils.hasOwner(this)
