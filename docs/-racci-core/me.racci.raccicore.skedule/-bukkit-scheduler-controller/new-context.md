//[RacciCore](../../../index.md)/[me.racci.raccicore.skedule](../index.md)/[BukkitSchedulerController](index.md)/[newContext](new-context.md)

# newContext

[jvm]\
suspend fun [newContext](new-context.md)(context: [SynchronizationContext](../-synchronization-context/index.md))

Force a new task to be scheduled in the specified context. This method will result in a new repeating or non-repeating task to be scheduled. Repetition state and resolution is determined by the currently running currentTask.

## Parameters

jvm

| | |
|---|---|
| context | the synchronization context of the new task |
