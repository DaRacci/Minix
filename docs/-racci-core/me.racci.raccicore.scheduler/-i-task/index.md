//[RacciCore](../../../index.md)/[me.racci.raccicore.scheduler](../index.md)/[ITask](index.md)

# ITask

[jvm]\
interface [ITask](index.md)

Represents a task being executed by the [CoroutineScheduler](../-coroutine-scheduler/index.md)

## Functions

| Name | Summary |
|---|---|
| [cancel](cancel.md) | [jvm]<br>abstract fun [cancel](cancel.md)()<br>Attempts to cancel the task. |

## Properties

| Name | Summary |
|---|---|
| [async](async.md) | [jvm]<br>abstract val [async](async.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Weather the task is being executed off the main bukkit thread. |
| [cancelled](cancelled.md) | [jvm]<br>abstract val [cancelled](cancelled.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>True if the task has been cancelled. |
| [owner](owner.md) | [jvm]<br>abstract val [owner](owner.md): [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md)<br>The plugin which starting and owns this task. |
| [taskID](task-i-d.md) | [jvm]<br>abstract val [taskID](task-i-d.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The unique ID of the task. |

## Inheritors

| Name |
|---|
| [CoroutineTask](../-coroutine-task/index.md) |
