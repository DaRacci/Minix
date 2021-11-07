//[RacciCore](../../../index.md)/[me.racci.raccicore.scheduler](../index.md)/[CoroutineTask](index.md)

# CoroutineTask

[jvm]\
@ApiStatus.Experimental

class [CoroutineTask](index.md) : [ITask](../-i-task/index.md)

## Constructors

| | |
|---|---|
| [CoroutineTask](-coroutine-task.md) | [jvm]<br>fun [CoroutineTask](-coroutine-task.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md), task: suspend [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md), CoroutineScope&gt;.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [CoroutineTask](-coroutine-task.md) | [jvm]<br>fun [CoroutineTask](-coroutine-task.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md), runnable: [CoroutineRunnable](../-coroutine-runnable/index.md)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [cancel](cancel.md) | [jvm]<br>open override fun [cancel](cancel.md)()<br>Attempts to cancel the task. |

## Properties

| Name | Summary |
|---|---|
| [async](async.md) | [jvm]<br>open override var [async](async.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false<br>Weather the task is being executed off the main bukkit thread. |
| [cancelled](cancelled.md) | [jvm]<br>open override val [cancelled](cancelled.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>True if the task has been cancelled. |
| [createdAt](created-at.md) | [jvm]<br>val [createdAt](created-at.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [owner](owner.md) | [jvm]<br>open override val [owner](owner.md): [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md)<br>The plugin which starting and owns this task. |
| [taskID](task-i-d.md) | [jvm]<br>open override var [taskID](task-i-d.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The unique ID of the task. |
