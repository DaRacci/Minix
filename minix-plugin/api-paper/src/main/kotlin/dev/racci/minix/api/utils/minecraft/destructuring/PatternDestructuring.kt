package dev.racci.minix.api.utils.minecraft.destructuring

import org.bukkit.DyeColor
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType

public operator fun Pattern.component1(): DyeColor = color
public operator fun Pattern.component2(): PatternType = pattern
