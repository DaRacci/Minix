//[RacciCore](../../../index.md)/[me.racci.raccicore.api.extensions](../index.md)/[BungeeCord](index.md)

# BungeeCord

[jvm]\
@[JvmInline](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-inline/index.html)

value class [BungeeCord](index.md)(player: Player)

## Functions

| Name | Summary |
|---|---|
| [kickPlayer](kick-player.md) | [jvm]<br>fun [kickPlayer](kick-player.md)(player: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), reason: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |
| [onlinePlayerAt](online-player-at.md) | [jvm]<br>suspend fun [onlinePlayerAt](online-player-at.md)(server: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = "ALL"): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>fun [onlinePlayerAt](online-player-at.md)(server: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = "ALL", callback: (playerCount: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [retrieveIp](retrieve-ip.md) | [jvm]<br>suspend fun [retrieveIp](retrieve-ip.md)(): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt;<br>fun [retrieveIp](retrieve-ip.md)(callback: (address: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), port: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [retrieveServerName](retrieve-server-name.md) | [jvm]<br>suspend fun [retrieveServerName](retrieve-server-name.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>fun [retrieveServerName](retrieve-server-name.md)(callback: (server: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [sendToServer](send-to-server.md) | [jvm]<br>fun [sendToServer](send-to-server.md)(server: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |
