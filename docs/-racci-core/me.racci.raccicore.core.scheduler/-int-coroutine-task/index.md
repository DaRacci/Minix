//[RacciCore](../../../index.md)/[me.racci.raccicore.core.scheduler](../index.md)/[IntCoroutineTask](index.md)

# IntCoroutineTask

[jvm]\
@ApiStatus.Internal

class [IntCoroutineTask](index.md) : [CoroutineTask](../../me.racci.raccicore.api.scheduler/-coroutine-task/index.md)

## Constructors

| | |
|---|---|
| [IntCoroutineTask](-int-coroutine-task.md) | [jvm]<br>fun [IntCoroutineTask](-int-coroutine-task.md)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), task: suspend [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), CoroutineScope&gt;.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [IntCoroutineTask](-int-coroutine-task.md) | [jvm]<br>fun [IntCoroutineTask](-int-coroutine-task.md)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), runnable: [CoroutineRunnable](../../me.racci.raccicore.api.scheduler/-coroutine-runnable/index.md)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [async](async.md) | [jvm]<br>open override fun [async](async.md)(): [IntCoroutineTask](index.md) |
| [cancel](cancel.md) | [jvm]<br>open override fun [cancel](cancel.md)()<br>Attempts to cancel the task. |
| [cancel0](cancel0.md) | [jvm]<br>suspend fun [cancel0](cancel0.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [sync](sync.md) | [jvm]<br>open override fun [sync](sync.md)(): [IntCoroutineTask](index.md) |

## Properties

| Name | Summary |
|---|---|
| [async](async.md) | [jvm]<br>open override var [async](async.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false<br>Weather the task is being executed off the main bukkit thread. |
| [cancelled](cancelled.md) | [jvm]<br>open override val [cancelled](cancelled.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>True if the task has been cancelled. |
| [job](job.md) | [jvm]<br>var [job](job.md): Job |
| [owner](owner.md) | [jvm]<br>open override val [owner](owner.md): [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md)<br>The plugin which starting and owns this task. |
| [period](period.md) | [jvm]<br>var [period](period.md): [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html) |
| [runnable](runnable.md) | [jvm]<br>var [runnable](runnable.md): [CoroutineRunnable](../../me.racci.raccicore.api.scheduler/-coroutine-runnable/index.md)? = null |
| [task](task.md) | [jvm]<br>var [task](task.md): suspend [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md), CoroutineScope&gt;.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [taskID](task-i-d.md) | [jvm]<br>open override var [taskID](task-i-d.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The unique ID of the task. |
