//[RacciCore](../../../index.md)/[me.racci.raccicore.skedule](../index.md)/[BukkitSchedulerController](index.md)/[yield](yield.md)

# yield

[jvm]\
suspend fun [yield](yield.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)

Relinquish control for as short an amount of time as possible. That is, wait for as few ticks as possible. If this coroutine is currently backed by a non-repeating task, this will result in a task running at the next possible occasion. If this coroutine is currently backed by a repeating task, this will result in a delay for as short an amount of ticks as the repetition resolution allows.

#### Return

the actual amount of ticks waited
