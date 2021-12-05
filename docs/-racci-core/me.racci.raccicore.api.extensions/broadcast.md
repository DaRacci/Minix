---
title: broadcast
---
//[RacciCore](../../index.html)/[me.racci.raccicore.api.extensions](index.html)/[broadcast](broadcast.html)



# broadcast



[jvm]\
fun [broadcast](broadcast.html)(component: Component): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)



Broadcast a component to the server.



## Parameters


jvm

| | |
|---|---|
| component | The component to broadcast |





[jvm]\
fun [broadcast](broadcast.html)(players: [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)&lt;Player&gt; = onlinePlayers, component: Player.() -&gt; Component)



Broadcast a component only some players



#### Receiver



## Parameters


jvm

| | |
|---|---|
| players | The players to broadcast to. |
| component | The Component to broadcast. |





[jvm]\
fun [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;Player&gt;.[broadcast](broadcast.html)(component: Player.() -&gt; Component)



Broadcast a component to the players of this [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)



## Parameters


jvm

| | |
|---|---|
| component | The component to broadcast |





[jvm]\
fun [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;Player&gt;.[broadcast](broadcast.html)(component: Player.() -&gt; Component)



Broadcast a component to the players of this [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)



## Parameters


jvm

| | |
|---|---|
| component | The component to broadcast |




