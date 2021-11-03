//[RacciCore](../../../index.md)/[me.racci.raccicore.skedule](../index.md)/[BukkitSchedulerController](index.md)/[repeating](repeating.md)

# repeating

[jvm]\
suspend fun [repeating](repeating.md)(resolution: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)

Turn this coroutine into a repeating coroutine. This method will result in a new repeating task being scheduled. The new task's interval will be the same as the specified resolution. Subsequent calls to [waitFor](wait-for.md) and [yield](yield.md) will from here on out defer further execution to the next iteration of the repeating task. This is useful for things like countdowns and delays at fixed intervals, since [waitFor](wait-for.md) will not result in a new task being spawned.
