package dev.racci.minix.api.destructors

import org.bukkit.block.banner.Pattern

operator fun Pattern.component1() = color
operator fun Pattern.component2() = pattern
