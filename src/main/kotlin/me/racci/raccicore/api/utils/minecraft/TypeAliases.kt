package me.racci.raccicore.api.utils.minecraft

import net.kyori.adventure.text.ComponentLike
import org.bukkit.entity.Player

typealias ResponseCallback = (message: ByteArray) -> Unit

typealias ChatInputCallBack = Player.(ComponentLike) -> Unit
typealias PlayerCallbackFunction<R> = Player.() -> R
typealias PlayerQuitFunction = PlayerCallbackFunction<Unit>
typealias PlayerMoveFunction = PlayerCallbackFunction<Boolean>