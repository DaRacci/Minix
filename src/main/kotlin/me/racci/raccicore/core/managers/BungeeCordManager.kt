package me.racci.raccicore.core.managers

import me.racci.raccicore.core.Provider
import me.racci.raccicore.core.RacciCore
import me.racci.raccicore.extensions.server
import me.racci.raccicore.lifecycle.LifecycleListener
import me.racci.raccicore.utils.BungeeCordUtils
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener
import java.nio.ByteBuffer
import java.nio.charset.Charset

class BungeeCordManager constructor(
    override val plugin: RacciCore,
): PluginMessageListener, LifecycleListener<RacciCore> {

    private val queue = mutableListOf<BungeeCordUtils.BungeeCordRequest>()

    override suspend fun onEnable() {
        Provider.bungeeCordManager = this
        server.messenger.registerOutgoingPluginChannel(plugin, "BungeeCord")
        server.messenger.registerIncomingPluginChannel(plugin, "BungeeCord", this)
    }

    override suspend fun onDisable() {
        queue.clear()
        server.messenger.unregisterOutgoingPluginChannel(plugin, "BungeeCord")
        server.messenger.unregisterIncomingPluginChannel(plugin, "BungeeCord", this)
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

    private fun ByteBuffer.readUTF() =
        String(ByteArray(short.toInt()).apply { get(this) }, Charset.forName("UTF-8"))
}