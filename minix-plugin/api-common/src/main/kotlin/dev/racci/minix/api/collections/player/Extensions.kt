package dev.racci.minix.api.collections.player

import dev.racci.minix.api.callbacks.PlayerQuitCallback
import dev.racci.minix.api.callbacks.PlayerQuitMapCallback
import dev.racci.minix.api.data.MinixPlayer
import dev.racci.minix.api.extensions.reflection.castOrThrow

public fun onlinePlayerListOf(
    vararg players: Pair<MinixPlayer, PlayerQuitCallback>
): OnlinePlayerList = OnlinePlayerList(players)

public fun onlinePlayerListOf(
    vararg players: MinixPlayer
): OnlinePlayerList = OnlinePlayerList(players.map { it to PlayerQuitCallback.empty }.toTypedArray())

public fun onlinePlayerSetOf(
    vararg players: Pair<MinixPlayer, PlayerQuitCallback>
): OnlinePlayerSet = OnlinePlayerSet(mutableMapOf(*players))

public fun onlinePlayerSetOf(
    vararg players: MinixPlayer
): OnlinePlayerSet = onlinePlayerSetOf(*players.map { it to PlayerQuitCallback.empty }.toTypedArray())

@JvmName("onlinePlayerMapOfPair")
public fun <V> onlinePlayerMapOf(
    vararg players: Pair<MinixPlayer, Pair<V, PlayerQuitMapCallback<V>>>
): OnlinePlayerMap<V> = OnlinePlayerMap(mutableMapOf(*players))

@JvmName("onlinePlayerMapOfTriple")
public fun <V> onlinePlayerMapOf(
    vararg players: Triple<MinixPlayer, V, PlayerQuitMapCallback<V>>
): OnlinePlayerMap<V> = onlinePlayerMapOf(*players.map { it.first to (it.second to it.third) }.toTypedArray())

@JvmName("onlinePlayerMapOfNoCallbackPair")
public fun <V> onlinePlayerMapOf(
    vararg players: Pair<MinixPlayer, V>
): OnlinePlayerMap<V> = onlinePlayerMapOf(*players.map { it.first to (it.second to PlayerQuitMapCallback.empty.castOrThrow<PlayerQuitMapCallback<V>>()) }.toTypedArray())
