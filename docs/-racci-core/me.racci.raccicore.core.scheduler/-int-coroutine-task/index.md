---
title: IntCoroutineTask
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.core.scheduler](../index.html)/[IntCoroutineTask](index.html)



# IntCoroutineTask



[jvm]\
@ApiStatus.Internal



class [IntCoroutineTask](index.html) : [CoroutineTask](../../me.racci.raccicore.api.scheduler/-coroutine-task/index.html)



## Constructors


| | |
|---|---|
| [IntCoroutineTask](-int-coroutine-task.html) | [jvm]<br>fun [IntCoroutineTask](-int-coroutine-task.html)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), task: suspend [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), CoroutineScope&gt;.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [IntCoroutineTask](-int-coroutine-task.html) | [jvm]<br>fun [IntCoroutineTask](-int-coroutine-task.html)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), runnable: [CoroutineRunnable](../../me.racci.raccicore.api.scheduler/-coroutine-runnable/index.html)) |


## Types


| Name | Summary |
|---|---|
| [Companion](-companion/index.html) | [jvm]<br>object [Companion](-companion/index.html) |


## Functions


| Name | Summary |
|---|---|
| [async](async.html) | [jvm]<br>open override fun [async](async.html)(): [IntCoroutineTask](index.html) |
| [cancel](cancel.html) | [jvm]<br>open override fun [cancel](cancel.html)()<br>Attempts to cancel the task. |
| [cancel0](cancel0.html) | [jvm]<br>suspend fun [cancel0](cancel0.html)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [sync](sync.html) | [jvm]<br>open override fun [sync](sync.html)(): [IntCoroutineTask](index.html) |


## Properties


| Name | Summary |
|---|---|
| [async](async.html) | [jvm]<br>open override var [async](async.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false<br>Weather the task is being executed off the main bukkit thread. |
| [cancelled](cancelled.html) | [jvm]<br>open override val [cancelled](cancelled.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>True if the task has been cancelled. |
| [job](job.html) | [jvm]<br>var [job](job.html): Job |
| [owner](owner.html) | [jvm]<br>open override val [owner](owner.html): [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html)<br>The plugin which starting and owns this task. |
| [period](period.html) | [jvm]<br>var [period](period.html): [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html) |
| [runnable](runnable.html) | [jvm]<br>var [runnable](runnable.html): [CoroutineRunnable](../../me.racci.raccicore.api.scheduler/-coroutine-runnable/index.html)? = null |
| [task](task.html) | [jvm]<br>var [task](task.html): suspend [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), CoroutineScope&gt;.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [taskID](task-i-d.html) | [jvm]<br>open override var [taskID](task-i-d.html): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The unique ID of the task. |

