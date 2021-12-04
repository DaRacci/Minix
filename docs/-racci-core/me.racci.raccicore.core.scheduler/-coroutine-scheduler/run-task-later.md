//[RacciCore](../../../index.md)/[me.racci.raccicore.core.scheduler](../index.md)/[CoroutineScheduler](index.md)/[runTaskLater](run-task-later.md)

# runTaskLater

[jvm]\
open override fun [runTaskLater](run-task-later.md)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), coroutineTask: [CoroutineTask](../../me.racci.raccicore.api.scheduler/-coroutine-task/index.md),
delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [CoroutineTask](../../me.racci.raccicore.api.scheduler/-coroutine-task/index.md)

Returns an [CoroutineTask](../../me.racci.raccicore.api.scheduler/-coroutine-task/index.md) that will run once on the main bukkit thread after the specified number of ticks in [delay](run-task-later.md) is reached.

#### Return

An [CoroutineTask](../../me.racci.raccicore.api.scheduler/-coroutine-task/index.md) that contains the id number.

## Parameters

jvm

| | |
|---|---|
| plugin | The [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md) who owns the task. |
| coroutineTask | The task to run. |

[jvm]\
open override fun [runTaskLater](run-task-later.md)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), task: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[RacciPlugin](
../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), CoroutineScope&gt;.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html),
delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [CoroutineTask](../../me.racci.raccicore.api.scheduler/-coroutine-task/index.md)

Returns an [CoroutineTask](../../me.racci.raccicore.api.scheduler/-coroutine-task/index.md) that will run once on the main bukkit thread after the specified number of ticks in [delay](run-task-later.md) is reached.

#### Return

An [CoroutineTask](../../me.racci.raccicore.api.scheduler/-coroutine-task/index.md) that contains the id number.

## Parameters

jvm

| | |
|---|---|
| plugin | The [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md) who owns the task. |
| task | The [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) to create a task from. |

[jvm]\
open override fun [runTaskLater](run-task-later.md)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), runnable: [CoroutineRunnable](../../me.racci.raccicore.api.scheduler/-coroutine-runnable/index.md),
delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [CoroutineTask](../../me.racci.raccicore.api.scheduler/-coroutine-task/index.md)

Returns an [CoroutineTask](../../me.racci.raccicore.api.scheduler/-coroutine-task/index.md) that will run once on the main bukkit thread after the specified number of ticks in [delay](run-task-later.md) is reached.

#### Return

An [CoroutineTask](../../me.racci.raccicore.api.scheduler/-coroutine-task/index.md) that contains the id number.

## Parameters

jvm

| | |
|---|---|
| plugin | The [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md) who owns the task. |
| runnable | The [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) to create a task from. |
