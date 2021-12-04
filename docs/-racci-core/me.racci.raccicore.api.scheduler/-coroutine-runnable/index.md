//[RacciCore](../../../index.md)/[me.racci.raccicore.api.scheduler](../index.md)/[CoroutineRunnable](index.md)

# CoroutineRunnable

[jvm]\
abstract class [CoroutineRunnable](index.md)

## Functions

| Name | Summary |
|---|---|
| [cancel](cancel.md) | [jvm]<br>fun [cancel](cancel.md)() |
| [run](run.md) | [jvm]<br>abstract suspend fun [run](run.md)() |
| [runAsyncTask](run-async-task.md) | [jvm]<br>fun [runAsyncTask](run-async-task.md)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md)): [CoroutineTask](../-coroutine-task/index.md) |
| [runAsyncTaskLater](run-async-task-later.md) | [jvm]<br>fun [runAsyncTaskLater](run-async-task-later.md)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [CoroutineTask](../-coroutine-task/index.md) |
| [runAsyncTaskTimer](run-async-task-timer.md) | [jvm]<br>fun [runAsyncTaskTimer](run-async-task-timer.md)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), period: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [CoroutineTask](../-coroutine-task/index.md) |
| [runTask](run-task.md) | [jvm]<br>fun [runTask](run-task.md)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md)): [CoroutineTask](../-coroutine-task/index.md) |
| [runTaskLater](run-task-later.md) | [jvm]<br>fun [runTaskLater](run-task-later.md)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [CoroutineTask](../-coroutine-task/index.md) |
| [runTaskTimer](run-task-timer.md) | [jvm]<br>fun [runTaskTimer](run-task-timer.md)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), period: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [CoroutineTask](../-coroutine-task/index.md) |

## Properties

| Name | Summary |
|---|---|
| [cancelled](cancelled.md) | [jvm]<br>val [cancelled](cancelled.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [plugin](plugin.md) | [jvm]<br>val [plugin](plugin.md): [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md)<br>This should only be used within the [run](run.md) stage, otherwise this will result in an [IllegalStateException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-state-exception/index.html) |
| [taskID](task-i-d.md) | [jvm]<br>val [taskID](task-i-d.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Inheritors

| Name |
|---|
| [TimeRunnable](../../me.racci.raccicore.core.runnables/-time-runnable/index.md) |
