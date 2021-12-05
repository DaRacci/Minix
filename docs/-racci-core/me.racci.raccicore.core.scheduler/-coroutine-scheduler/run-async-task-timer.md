---
title: runAsyncTaskTimer
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.core.scheduler](../index.html)/[CoroutineScheduler](index.html)/[runAsyncTaskTimer](run-async-task-timer.html)



# runAsyncTaskTimer



[jvm]\
open override fun [runAsyncTaskTimer](run-async-task-timer.html)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), coroutineTask: [CoroutineTask](../../me.racci.raccicore.api.scheduler/-coroutine-task/index.html), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), period: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [CoroutineTask](../../me.racci.raccicore.api.scheduler/-coroutine-task/index.html)



Returns an [CoroutineTask](../../me.racci.raccicore.api.scheduler/-coroutine-task/index.html) that will run once off the main bukkit thread after the specified number of ticks in [delay](run-async-task-timer.html) is reached and will repeat every [period](run-async-task-timer.html) ticks until cancelled.



#### Return



An [CoroutineTask](../../me.racci.raccicore.api.scheduler/-coroutine-task/index.html) that contains the id number.



## Parameters


jvm

| | |
|---|---|
| plugin | The [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html) who owns the task. |
| coroutineTask | The task to run. |





[jvm]\
open override fun [runAsyncTaskTimer](run-async-task-timer.html)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), task: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), CoroutineScope&gt;.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), period: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [CoroutineTask](../../me.racci.raccicore.api.scheduler/-coroutine-task/index.html)



Returns an [CoroutineTask](../../me.racci.raccicore.api.scheduler/-coroutine-task/index.html) that will run once off the main bukkit thread after the specified number of ticks in [delay](run-async-task-timer.html) is reached and will repeat every [period](run-async-task-timer.html) ticks until cancelled.



#### Return



An [CoroutineTask](../../me.racci.raccicore.api.scheduler/-coroutine-task/index.html) that contains the id number.



## Parameters


jvm

| | |
|---|---|
| plugin | The [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html) who owns the task. |
| task | The [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) to create a task from. |





[jvm]\
open override fun [runAsyncTaskTimer](run-async-task-timer.html)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html), runnable: [CoroutineRunnable](../../me.racci.raccicore.api.scheduler/-coroutine-runnable/index.html), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), period: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [CoroutineTask](../../me.racci.raccicore.api.scheduler/-coroutine-task/index.html)



Returns an [CoroutineTask](../../me.racci.raccicore.api.scheduler/-coroutine-task/index.html) that will run once off the main bukkit thread after the specified number of ticks in [delay](run-async-task-timer.html) is reached and will repeat every [period](run-async-task-timer.html) ticks until cancelled.



#### Return



An [CoroutineTask](../../me.racci.raccicore.api.scheduler/-coroutine-task/index.html) that contains the id number.



## Parameters


jvm

| | |
|---|---|
| plugin | The [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html) who owns the task. |
| runnable | The [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) to create a task from. |




