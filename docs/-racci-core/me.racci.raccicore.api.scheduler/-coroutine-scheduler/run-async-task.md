//[RacciCore](../../../index.md)/[me.racci.raccicore.api.scheduler](../index.md)/[CoroutineScheduler](index.md)/[runAsyncTask](run-async-task.md)

# runAsyncTask

[jvm]\
abstract fun [runAsyncTask](run-async-task.md)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), coroutineTask: [CoroutineTask](../-coroutine-task/index.md)): [CoroutineTask](../-coroutine-task/index.md)

Returns an [CoroutineTask](../-coroutine-task/index.md) that will run once off the main bukkit thread.

#### Return

An [CoroutineTask](../-coroutine-task/index.md) that contains the id number

## Parameters

jvm

| | |
|---|---|
| plugin | The [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md) who owns the task. |
| coroutineTask | The task to run. |

[jvm]\
abstract fun [runAsyncTask](run-async-task.md)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), task: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[RacciPlugin](
../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), CoroutineScope&gt;.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [CoroutineTask](../-coroutine-task/index.md)

Returns an [CoroutineTask](../-coroutine-task/index.md) that will run once off the main bukkit thread.

#### Return

An [CoroutineTask](../-coroutine-task/index.md) that contains the id number.

## Parameters

jvm

| | |
|---|---|
| plugin | The [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md) who owns the task. |
| task | The [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) to create a task from. |

[jvm]\
abstract fun [runAsyncTask](run-async-task.md)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), runnable: [CoroutineRunnable](../-coroutine-runnable/index.md)): [CoroutineTask](../-coroutine-task/index.md)

Returns an [CoroutineTask](../-coroutine-task/index.md) that will run once off the main bukkit thread.

#### Return

An [CoroutineTask](../-coroutine-task/index.md) that contains the id number.

## Parameters

jvm

| | |
|---|---|
| plugin | The [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md) who owns the task. |
| runnable | The [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) to create a task from. |
