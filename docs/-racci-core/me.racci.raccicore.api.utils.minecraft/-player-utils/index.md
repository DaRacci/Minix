---
title: PlayerUtils
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.minecraft](../index.html)/[PlayerUtils](index.html)



# PlayerUtils



[jvm]\
object [PlayerUtils](index.html)

Utilities for Players



## Types


| Name | Summary |
|---|---|
| [ChatInput](-chat-input/index.html) | [jvm]<br>class [ChatInput](-chat-input/index.html)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), callback: [ChatInputCallBack](../index.html#-38014740%2FClasslikes%2F863300109), playerQuit: [PlayerQuitFunction](../index.html#-400580617%2FClasslikes%2F863300109)) |
| [PlayerCallback](-player-callback/index.html) | [jvm]<br>class [PlayerCallback](-player-callback/index.html)&lt;[R](-player-callback/index.html)&gt;(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), callback: [PlayerCallbackFunction](../index.html#38361665%2FClasslikes%2F863300109)&lt;[R](-player-callback/index.html)&gt;) |


## Functions


| Name | Summary |
|---|---|
| [chatInput](chat-input.html) | [jvm]<br>suspend fun Player.[chatInput](chat-input.html)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html)): ComponentLike?<br>fun Player.[chatInput](chat-input.html)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), whenQuitWithoutInput: [PlayerQuitFunction](../index.html#-400580617%2FClasslikes%2F863300109) = {}, callback: [ChatInputCallBack](../index.html#-38014740%2FClasslikes%2F863300109)) |
| [whenMove](when-move.html) | [jvm]<br>fun Player.[whenMove](when-move.html)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), callback: [PlayerMoveFunction](../index.html#-2077606219%2FClasslikes%2F863300109)) |
| [whenQuit](when-quit.html) | [jvm]<br>fun Player.[whenQuit](when-quit.html)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), callback: [PlayerQuitFunction](../index.html#-400580617%2FClasslikes%2F863300109)) |

