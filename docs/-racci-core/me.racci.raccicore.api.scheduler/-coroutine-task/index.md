---
title: CoroutineTask
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.scheduler](../index.html)/[CoroutineTask](index.html)



# CoroutineTask



[jvm]\
interface [CoroutineTask](index.html)

Represents a task being executed by the [CoroutineScheduler](../-coroutine-scheduler/index.html)



## Functions


| Name | Summary |
|---|---|
| [async](async.html) | [jvm]<br>abstract fun [async](async.html)(): [CoroutineTask](index.html) |
| [cancel](cancel.html) | [jvm]<br>abstract fun [cancel](cancel.html)()<br>Attempts to cancel the task. |
| [sync](sync.html) | [jvm]<br>abstract fun [sync](sync.html)(): [CoroutineTask](index.html) |


## Properties


| Name | Summary |
|---|---|
| [async](async.html) | [jvm]<br>abstract val [async](async.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Weather the task is being executed off the main bukkit thread. |
| [cancelled](cancelled.html) | [jvm]<br>abstract val [cancelled](cancelled.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>True if the task has been cancelled. |
| [owner](owner.html) | [jvm]<br>abstract val [owner](owner.html): [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html)<br>The plugin which starting and owns this task. |
| [taskID](task-i-d.html) | [jvm]<br>abstract var [taskID](task-i-d.html): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The unique ID of the task. |


## Inheritors


| Name |
|---|
| [IntCoroutineTask](../../me.racci.raccicore.core.scheduler/-int-coroutine-task/index.html) |

