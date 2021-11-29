//[RacciCore](../../../index.md)/[me.racci.raccicore.core.scheduler](../index.md)/[ICoroutineRunnable](index.md)

# ICoroutineRunnable

[jvm]\
interface [ICoroutineRunnable](index.md)

## Functions

| Name | Summary |
|---|---|
| [cancel](cancel.md) | [jvm]<br>abstract fun [cancel](cancel.md)() |
| [run](run.md) | [jvm]<br>abstract suspend fun [run](run.md)() |
| [runAsyncTaskLater](run-async-task-later.md) | [jvm]<br>abstract fun [runAsyncTaskLater](run-async-task-later.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [ITask](../-i-task/index.md) |
| [runAsyncTaskTimer](run-async-task-timer.md) | [jvm]<br>abstract fun [runAsyncTaskTimer](run-async-task-timer.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), period: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [ITask](../-i-task/index.md) |
| [runTask](run-task.md) | [jvm]<br>abstract fun [runTask](run-task.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md)): [ITask](../-i-task/index.md) |
| [runTaskAsync](run-task-async.md) | [jvm]<br>abstract fun [runTaskAsync](run-task-async.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md)): [ITask](../-i-task/index.md) |
| [runTaskLater](run-task-later.md) | [jvm]<br>abstract fun [runTaskLater](run-task-later.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [ITask](../-i-task/index.md) |
| [runTaskTimer](run-task-timer.md) | [jvm]<br>abstract fun [runTaskTimer](run-task-timer.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), period: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [ITask](../-i-task/index.md) |

## Properties

| Name | Summary |
|---|---|
| [cancelled](cancelled.md) | [jvm]<br>abstract val [cancelled](cancelled.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [taskID](task-i-d.md) | [jvm]<br>abstract val [taskID](task-i-d.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Inheritors

| Name |
|---|
| [CoroutineRunnable](../-coroutine-runnable/index.md) |
