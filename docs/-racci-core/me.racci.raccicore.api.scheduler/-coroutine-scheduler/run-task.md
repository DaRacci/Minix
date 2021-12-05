---
title: runTask
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.scheduler](../index.html)/[CoroutineScheduler](index.html)/[runTask](run-task.html)



# runTask



[jvm]\
abstract fun [runTask](run-task.html)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), coroutineTask: [CoroutineTask](../-coroutine-task/index.html)): [CoroutineTask](../-coroutine-task/index.html)



Returns an [CoroutineTask](../-coroutine-task/index.html) that will run once on the main bukkit thread.



#### Return



An [CoroutineTask](../-coroutine-task/index.html) that contains the id number



## Parameters


jvm

| | |
|---|---|
| plugin | The [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html) who owns the task. |
| coroutineTask | The task to run. |





[jvm]\
abstract fun [runTask](run-task.html)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), task: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), CoroutineScope&gt;.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [CoroutineTask](../-coroutine-task/index.html)



Returns an [CoroutineTask](../-coroutine-task/index.html) that will run once on the main bukkit thread.



#### Return



An [CoroutineTask](../-coroutine-task/index.html) that contains the id number.



## Parameters


jvm

| | |
|---|---|
| plugin | The [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html) who owns the task. |
| task | The [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) to create a task from. |





[jvm]\
abstract fun [runTask](run-task.html)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), runnable: [CoroutineRunnable](../-coroutine-runnable/index.html)): [CoroutineTask](../-coroutine-task/index.html)



Returns an [CoroutineTask](../-coroutine-task/index.html) that will run once on the main bukkit thread.



#### Return



An [CoroutineTask](../-coroutine-task/index.html) that contains the id number.



## Parameters


jvm

| | |
|---|---|
| plugin | The [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html) who owns the task. |
| runnable | The [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) to create a task from. |




