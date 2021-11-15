@file:Suppress("UnstableApiUsage", "UnstableApiUsage")

package me.racci.raccicore.utils

import com.google.common.io.ByteStreams
import me.racci.raccicore.core.Provider
import me.racci.raccicore.extensions.sendBungeeCord
import org.bukkit.entity.Player

typealias ResponseCallback = (message: ByteArray) -> Unit

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
            if(responseCallback != null) Provider.bungeeCordManager.addToQueue(this)
        }
    }

}