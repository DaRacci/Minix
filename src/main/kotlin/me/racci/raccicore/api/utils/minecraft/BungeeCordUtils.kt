@file:Suppress("UnstableApiUsage", "UNUSED", "MemberVisibilityCanBePrivate")

package me.racci.raccicore.api.utils.minecraft

import com.google.common.io.ByteStreams
import me.racci.raccicore.api.extensions.sendBungeeCord
import me.racci.raccicore.core.managers.BungeeCordManager
import org.bukkit.entity.Player

/**
 * Utilities for BungeeCord
 */
object BungeeCordUtils {

    class BungeeCordRequest(
        val player: Player,
        val subChannel: String,
        val request: ByteArray? = null,
        val responseCallback: ResponseCallback? = null
    ) {
        fun send() {
            val out = ByteStreams.newDataOutput()
            out.writeUTF(subChannel)
            if(request != null) out.write(request)
            player.sendBungeeCord(out.toByteArray())
            if(responseCallback != null) BungeeCordManager.addToQueue(this)
        }
    }

}