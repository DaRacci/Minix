//[RacciCore](../../../index.md)/[me.racci.raccicore.skedule](../index.md)/[BukkitSchedulerController](index.md)/[switchContext](switch-context.md)

# switchContext

[jvm]\
suspend fun [switchContext](switch-context.md)(context: [SynchronizationContext](../-synchronization-context/index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

Switch to the specified SynchronizationContext. If this coroutine is already in the given context, this method does nothing and returns immediately. Otherwise, the behaviour is documented in [newContext](new-context.md).

#### Return

true if a context switch was made, false otherwise

## Parameters

jvm

| | |
|---|---|
| context | the context to switch to |
