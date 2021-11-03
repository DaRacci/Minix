//[RacciCore](../../../index.md)/[me.racci.raccicore.skedule](../index.md)/[TaskScheduler](index.md)

# TaskScheduler

[jvm]\
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")

~~interface~~ [~~TaskScheduler~~](index.md)

## Functions

| Name | Summary |
|---|---|
| [doContextSwitch](do-context-switch.md) | [jvm]<br>abstract fun [doContextSwitch](do-context-switch.md)(context: [SynchronizationContext](../-synchronization-context/index.md), task: ([Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [doWait](do-wait.md) | [jvm]<br>abstract fun [doWait](do-wait.md)(ticks: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), task: ([Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [doYield](do-yield.md) | [jvm]<br>abstract fun [doYield](do-yield.md)(task: ([Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [forceNewContext](force-new-context.md) | [jvm]<br>abstract fun [forceNewContext](force-new-context.md)(context: [SynchronizationContext](../-synchronization-context/index.md), task: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [currentTask](current-task.md) | [jvm]<br>abstract val [currentTask](current-task.md): BukkitTask? |
