package dev.racci.minix.api.aliases

import net.kyori.adventure.text.ComponentLike
import org.bukkit.entity.Player

typealias ChatInputCallBack = Player.(ComponentLike) -> Unit
typealias PlayerCallbackFunction<R> = Player.() -> R
typealias PlayerQuitFunction = PlayerCallbackFunction<Unit>
typealias PlayerMoveFunction = PlayerCallbackFunction<Boolean>
