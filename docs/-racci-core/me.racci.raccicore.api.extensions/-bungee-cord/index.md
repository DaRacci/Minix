---
title: BungeeCord
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.extensions](../index.html)/[BungeeCord](index.html)



# BungeeCord



[jvm]\
@[JvmInline](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-inline/index.html)



value class [BungeeCord](index.html)(player: Player)



## Functions


| Name | Summary |
|---|---|
| [kickPlayer](kick-player.html) | [jvm]<br>fun [kickPlayer](kick-player.html)(player: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), reason: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |
| [onlinePlayerAt](online-player-at.html) | [jvm]<br>suspend fun [onlinePlayerAt](online-player-at.html)(server: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = "ALL"): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>fun [onlinePlayerAt](online-player-at.html)(server: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = "ALL", callback: (playerCount: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [retrieveIp](retrieve-ip.html) | [jvm]<br>suspend fun [retrieveIp](retrieve-ip.html)(): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt;<br>fun [retrieveIp](retrieve-ip.html)(callback: (address: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), port: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [retrieveServerName](retrieve-server-name.html) | [jvm]<br>suspend fun [retrieveServerName](retrieve-server-name.html)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>fun [retrieveServerName](retrieve-server-name.html)(callback: (server: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [sendToServer](send-to-server.html) | [jvm]<br>fun [sendToServer](send-to-server.html)(server: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |

