---
title: cancelTask
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.scheduler](../index.html)/[CoroutineScheduler](index.html)/[cancelTask](cancel-task.html)



# cancelTask



[jvm]\
abstract suspend fun [cancelTask](cancel-task.html)(taskID: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)



Attempts to remove a task from the scheduler.



#### Return



If the task was successfully cancelled and removed.



## Parameters


jvm

| | |
|---|---|
| taskID | The task to remove and cancel. |




