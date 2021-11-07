//[RacciCore](../../../index.md)/[me.racci.raccicore.scheduler](../index.md)/[ICoroutineScheduler](index.md)/[runTaskLater](run-task-later.md)

# runTaskLater

[jvm]\
abstract fun [runTaskLater](run-task-later.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md), coroutineTask: [CoroutineTask](../-coroutine-task/index.md), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [ITask](../-i-task/index.md)

Returns an [ITask](../-i-task/index.md) that will run once on the main bukkit thread after the specified number of ticks in [delay](run-task-later.md) is reached.

#### Return

An [ITask](../-i-task/index.md) that contains the id number.

## Parameters

jvm

| | |
|---|---|
| plugin | The [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md) who owns the task. |
| coroutineTask | The task to run. |

[jvm]\
abstract fun [runTaskLater](run-task-later.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md), task: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md), CoroutineScope&gt;.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [ITask](../-i-task/index.md)

Returns an [ITask](../-i-task/index.md) that will run once on the main bukkit thread after the specified number of ticks in [delay](run-task-later.md) is reached.

#### Return

An [ITask](../-i-task/index.md) that contains the id number.

## Parameters

jvm

| | |
|---|---|
| plugin | The [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md) who owns the task. |
| task | The [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) to create a task from. |

[jvm]\
abstract fun [runTaskLater](run-task-later.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md), runnable: [CoroutineRunnable](../-coroutine-runnable/index.md), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [ITask](../-i-task/index.md)

Returns an [ITask](../-i-task/index.md) that will run once on the main bukkit thread after the specified number of ticks in [delay](run-task-later.md) is reached.

#### Return

An [ITask](../-i-task/index.md) that contains the id number.

## Parameters

jvm

| | |
|---|---|
| plugin | The [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md) who owns the task. |
| runnable | The [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) to create a task from. |
