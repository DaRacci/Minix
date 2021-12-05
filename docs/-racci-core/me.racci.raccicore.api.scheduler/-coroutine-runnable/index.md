---
title: CoroutineRunnable
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.scheduler](../index.html)/[CoroutineRunnable](index.html)



# CoroutineRunnable



[jvm]\
abstract class [CoroutineRunnable](index.html)



## Functions


| Name | Summary |
|---|---|
| [cancel](cancel.html) | [jvm]<br>fun [cancel](cancel.html)() |
| [run](run.html) | [jvm]<br>abstract suspend fun [run](run.html)() |
| [runAsyncTask](run-async-task.html) | [jvm]<br>fun [runAsyncTask](run-async-task.html)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html)): [CoroutineTask](../-coroutine-task/index.html) |
| [runAsyncTaskLater](run-async-task-later.html) | [jvm]<br>fun [runAsyncTaskLater](run-async-task-later.html)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [CoroutineTask](../-coroutine-task/index.html) |
| [runAsyncTaskTimer](run-async-task-timer.html) | [jvm]<br>fun [runAsyncTaskTimer](run-async-task-timer.html)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), period: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [CoroutineTask](../-coroutine-task/index.html) |
| [runTask](run-task.html) | [jvm]<br>fun [runTask](run-task.html)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html)): [CoroutineTask](../-coroutine-task/index.html) |
| [runTaskLater](run-task-later.html) | [jvm]<br>fun [runTaskLater](run-task-later.html)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [CoroutineTask](../-coroutine-task/index.html) |
| [runTaskTimer](run-task-timer.html) | [jvm]<br>fun [runTaskTimer](run-task-timer.html)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), period: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [CoroutineTask](../-coroutine-task/index.html) |


## Properties


| Name | Summary |
|---|---|
| [cancelled](cancelled.html) | [jvm]<br>val [cancelled](cancelled.html): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [plugin](plugin.html) | [jvm]<br>val [plugin](plugin.html): [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html)<br>This should only be used within the [run](run.html) stage, otherwise this will result in an [IllegalStateException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-state-exception/index.html) |
| [taskID](task-i-d.html) | [jvm]<br>val [taskID](task-i-d.html): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |


## Inheritors


| Name |
|---|
| [TimeRunnable](../../me.racci.raccicore.core.runnables/-time-runnable/index.html) |

