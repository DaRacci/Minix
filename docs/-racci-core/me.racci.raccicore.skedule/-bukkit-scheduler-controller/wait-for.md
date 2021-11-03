//[RacciCore](../../../index.md)/[me.racci.raccicore.skedule](../index.md)/[BukkitSchedulerController](index.md)/[waitFor](wait-for.md)

# waitFor

[jvm]\
suspend fun [waitFor](wait-for.md)(ticks: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)

Wait for **at least** the specified amount of ticks. If the coroutine is currently backed by a non-repeating task, a new Bukkit task will be scheduled to run the specified amount of ticks later. If this coroutine is currently backed by a repeating task, the amount of ticks waited depends on the repetition resolution of the coroutine. For example, if the repetition resolution is 10 and the ticks argument is 12, it will result in a delay of 20 ticks.

#### Return

the actual amount of ticks waited

## Parameters

jvm

| | |
|---|---|
| ticks | the amount of ticks to **at least** wait for |
