package dev.racci.minix.api.plugin

import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.jetbrains.annotations.ApiStatus

/**
 * Used to interface with the internal Minix class
 * from the API.
 */
@ApiStatus.Internal
@ApiStatus.NonExtendable
abstract class Minix : MinixPlugin() {
    abstract val adventure: Lazy<BukkitAudiences>
}
