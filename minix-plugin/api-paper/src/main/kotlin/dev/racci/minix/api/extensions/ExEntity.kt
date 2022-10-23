package dev.racci.minix.api.extensions

import dev.racci.minix.api.utils.minecraft.TameUtils
import org.bukkit.entity.Entity

public fun Entity.isTamed(): Boolean = TameUtils.isTamed(this)

public fun Entity.hasOwner(): Boolean = TameUtils.hasOwner(this)
