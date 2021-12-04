//[RacciCore](../../../index.md)/[me.racci.raccicore.api.utils.minecraft](../index.md)/[PlayerUtils](index.md)

# PlayerUtils

[jvm]\
object [PlayerUtils](index.md)

Utilities for Players

## Types

| Name | Summary |
|---|---|
| [ChatInput](-chat-input/index.md) | [jvm]<br>class [ChatInput](-chat-input/index.md)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), callback: [ChatInputCallBack](../index.md#-38014740%2FClasslikes%2F-1216412040), playerQuit: [PlayerQuitFunction](../index.md#-400580617%2FClasslikes%2F-1216412040)) |
| [PlayerCallback](-player-callback/index.md) | [jvm]<br>class [PlayerCallback](-player-callback/index.md)&lt;[R](-player-callback/index.md)&gt;(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), callback: [PlayerCallbackFunction](../index.md#38361665%2FClasslikes%2F-1216412040)&lt;[R](-player-callback/index.md)&gt;) |

## Functions

| Name | Summary |
|---|---|
| [chatInput](chat-input.md) | [jvm]<br>suspend fun Player.[chatInput](chat-input.md)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md)): ComponentLike?<br>fun Player.[chatInput](chat-input.md)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), whenQuitWithoutInput: [PlayerQuitFunction](../index.md#-400580617%2FClasslikes%2F-1216412040) = {}, callback: [ChatInputCallBack](../index.md#-38014740%2FClasslikes%2F-1216412040)) |
| [whenMove](when-move.md) | [jvm]<br>fun Player.[whenMove](when-move.md)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), callback: [PlayerMoveFunction](../index.md#-2077606219%2FClasslikes%2F-1216412040)) |
| [whenQuit](when-quit.md) | [jvm]<br>fun Player.[whenQuit](when-quit.md)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), callback: [PlayerQuitFunction](../index.md#-400580617%2FClasslikes%2F-1216412040)) |
