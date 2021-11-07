//[RacciCore](../../../index.md)/[me.racci.raccicore.scheduler](../index.md)/[ICoroutineScheduler](index.md)/[isCurrentlyRunning](is-currently-running.md)

# isCurrentlyRunning

[jvm]\
abstract fun [isCurrentlyRunning](is-currently-running.md)(taskID: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

Check if the task is currently active. A task that has finished and does not repeat, will not be active ever again.

#### Return

If the task is currently active.

## Parameters

jvm

| | |
|---|---|
| taskID | The task to check |
