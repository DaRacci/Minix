---
title: isCurrentlyRunning
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.core.scheduler](../index.html)/[CoroutineScheduler](index.html)/[isCurrentlyRunning](is-currently-running.html)



# isCurrentlyRunning



[jvm]\
open override fun [isCurrentlyRunning](is-currently-running.html)(taskID: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)



Check if the task is currently active. A task that has finished and does not repeat, will not be active ever again.



#### Return



If the task is currently active.



## Parameters


jvm

| | |
|---|---|
| taskID | The task to check |




