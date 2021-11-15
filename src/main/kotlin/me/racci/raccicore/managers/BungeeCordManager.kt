package me.racci.raccicore.managers

import me.racci.raccicore.core.RacciCore
import me.racci.raccicore.extensions.server
import me.racci.raccicore.utils.BungeeCordUtils
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener
import java.nio.ByteBuffer
import java.nio.charset.Charset

internal object BungeeCordManager: PluginMessageListener {

    private val queue = mutableListOf<BungeeCordUtils.BungeeCordRequest>()

    fun init() {
        server.messenger.registerOutgoingPluginChannel(RacciCore.instance, "BungeeCord")
        server.messenger.registerIncomingPluginChannel(RacciCore.instance, "BungeeCord", this)
    }

    override fun onPluginMessageReceived(
        channel: String,
        player: Player,
        message: ByteArray
    ) {
        if (channel != "BungeeCord") return

        val buffer = ByteBuffer.wrap(message)
        val subChannel = buffer.readUTF()
        val request = queue.firstOrNull{it.subChannel == subChannel}
        if(request?.responseCallback != null) {
            val infoBuffer = buffer.slice()
            val info = ByteArray(infoBuffer.remaining())
            infoBuffer.get(info)
            request.responseCallback.invoke(info)
            queue.remove(request)
        }
    }

    fun sendBungeeCord(
        player: Player,
        message: ByteArray
    ) = player.sendPluginMessage(RacciCore.instance, "BungeeCord", message)

    fun addToQueue(
        request: BungeeCordUtils.BungeeCordRequest
    ) = queue.add(request)

    private fun ByteBuffer.readUTF() = String(ByteArray(short.toInt()).apply { get(this) }, Charset.forName("UTF-8"))
}