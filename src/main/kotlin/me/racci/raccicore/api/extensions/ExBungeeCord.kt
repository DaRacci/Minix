@file:Suppress("UNUSED", "MemberVisibilityCanBePrivate", "UnstableApiUsage")
package me.racci.raccicore.api.extensions

import com.google.common.io.ByteStreams
import me.racci.raccicore.api.utils.minecraft.BungeeCordUtils
import me.racci.raccicore.core.managers.BungeeCordManager
import org.bukkit.entity.Player
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

val Player.bungeecord get() = BungeeCord(this)

fun Player.sendBungeeCord(message: ByteArray) = BungeeCordManager.sendBungeeCord(this, message)

@JvmInline
value class BungeeCord(
    private val player: Player
) {

    fun sendToServer(
        server: String
    ) = BungeeCordUtils.BungeeCordRequest(
        player,
        "Connect",
        ByteStreams.newDataOutput().apply {writeUTF(server)}.toByteArray()
    ).send()

    fun retrieveIp(
        callback: (address: String, port: Int) -> Unit
    ) = BungeeCordUtils.BungeeCordRequest(player, "IP") {
        val input = ByteStreams.newDataInput(it)
        val ip = input.readUTF()
        val port = input.readInt()
        callback(ip, port)
    }.send()

    suspend fun retrieveIp(): Pair<String, Int> = suspendCoroutine { continuation ->
        retrieveIp { address, port ->  continuation.resume(address to port) }
    }

    fun onlinePlayerAt(
        server: String = "ALL",
        callback: (playerCount: Int) -> Unit
    ) = BungeeCordUtils.BungeeCordRequest(player, "PlayerCount", ByteStreams.newDataOutput().apply {writeUTF(server)}.toByteArray()) {
        val input = ByteStreams.newDataInput(it)
        val serverSendTo = input.readUTF()
        val playerCount = input.readInt()
        callback(playerCount)
    }.send()

    suspend fun onlinePlayerAt(
        server: String = "ALL"
    ): Int = suspendCoroutine { continuation ->
        onlinePlayerAt {  continuation.resume(it) }
    }

    fun retrieveServerName(
        callback: (server: String) -> Unit
    ) = BungeeCordUtils.BungeeCordRequest(player, "GetServer") {
        val input = ByteStreams.newDataInput(it)
        val server = input.readUTF()
        callback(server)
    }.send()

    suspend fun retrieveServerName(): String = suspendCoroutine { continuation ->
        retrieveServerName {  continuation.resume(it) }
    }

    fun kickPlayer(
        player: String,
        reason: String
    ) = BungeeCordUtils.BungeeCordRequest(this.player, "KickPlayer",
        ByteStreams.newDataOutput().apply {writeUTF(player);writeUTF(reason)}.toByteArray()).send()
}