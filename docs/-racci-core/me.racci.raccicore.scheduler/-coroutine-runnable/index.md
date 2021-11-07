//[RacciCore](../../../index.md)/[me.racci.raccicore.scheduler](../index.md)/[CoroutineRunnable](index.md)

# CoroutineRunnable

[jvm]\
@ApiStatus.Experimental

abstract class [CoroutineRunnable](index.md) : [ICoroutineRunnable](../-i-coroutine-runnable/index.md)

## Functions

| Name | Summary |
|---|---|
| [cancel](cancel.md) | [jvm]<br>open override fun [cancel](cancel.md)() |
| [run](../-i-coroutine-runnable/run.md) | [jvm]<br>abstract suspend fun [run](../-i-coroutine-runnable/run.md)() |
| [runAsyncTaskLater](run-async-task-later.md) | [jvm]<br>open override fun [runAsyncTaskLater](run-async-task-later.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [ITask](../-i-task/index.md) |
| [runAsyncTaskTimer](run-async-task-timer.md) | [jvm]<br>open override fun [runAsyncTaskTimer](run-async-task-timer.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), period: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [ITask](../-i-task/index.md) |
| [runTask](run-task.md) | [jvm]<br>open override fun [runTask](run-task.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md)): [ITask](../-i-task/index.md) |
| [runTaskAsync](run-task-async.md) | [jvm]<br>open override fun [runTaskAsync](run-task-async.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md)): [ITask](../-i-task/index.md) |
| [runTaskLater](run-task-later.md) | [jvm]<br>open override fun [runTaskLater](run-task-later.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [ITask](../-i-task/index.md) |
| [runTaskTimer](run-task-timer.md) | [jvm]<br>open override fun [runTaskTimer](run-task-timer.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), period: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [ITask](../-i-task/index.md) |

## Properties

| Name | Summary |
|---|---|
| [cancelled](cancelled.md) | [jvm]<br>open override val [cancelled](cancelled.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [plugin](plugin.md) | [jvm]<br>val [plugin](plugin.md): [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md)<br>This should only be used within the run stage, otherwise this will result in an [IllegalStateException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-state-exception/index.html) |
| [taskID](task-i-d.md) | [jvm]<br>open override val [taskID](task-i-d.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Inheritors

| Name |
|---|
| [TimeRunnable](../../me.racci.raccicore.runnables/-time-runnable/index.md) |
