@file:JvmName("PlayerData")
package me.racci.raccilib.data

import net.kyori.adventure.text.Component
import java.util.*

abstract class PlayerData(
    val uuid: UUID,
    val userName: String,
    val displayName: Component,

    )
