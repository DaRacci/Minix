//[RacciCore](../../../index.md)/[me.racci.raccicore.skedule](../index.md)/[BukkitSchedulerController](index.md)

# BukkitSchedulerController

[jvm]\
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")

~~class~~ [~~BukkitSchedulerController~~](index.md)~~(~~~~plugin~~~~:~~ [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md)~~,~~ ~~scheduler~~~~:~~ BukkitScheduler~~)~~ ~~:~~ [~~Continuation~~](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-continuation/index.html)~~&lt;~~[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)~~&gt;~~ 

Controller for Bukkit Scheduler coroutine

## Constructors

| | |
|---|---|
| [BukkitSchedulerController](-bukkit-scheduler-controller.md) | [jvm]<br>fun [BukkitSchedulerController](-bukkit-scheduler-controller.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md), scheduler: BukkitScheduler) |

## Functions

| Name | Summary |
|---|---|
| [newContext](new-context.md) | [jvm]<br>suspend fun [newContext](new-context.md)(context: [SynchronizationContext](../-synchronization-context/index.md))<br>Force a new task to be scheduled in the specified context. This method will result in a new repeating or non-repeating task to be scheduled. Repetition state and resolution is determined by the currently running currentTask. |
| [repeating](repeating.md) | [jvm]<br>suspend fun [repeating](repeating.md)(resolution: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>Turn this coroutine into a repeating coroutine. This method will result in a new repeating task being scheduled. The new task's interval will be the same as the specified resolution. Subsequent calls to [waitFor](wait-for.md) and [yield](yield.md) will from here on out defer further execution to the next iteration of the repeating task. This is useful for things like countdowns and delays at fixed intervals, since [waitFor](wait-for.md) will not result in a new task being spawned. |
| [resumeWith](resume-with.md) | [jvm]<br>open override fun [resumeWith](resume-with.md)(result: [Result](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/index.html)&lt;[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;) |
| [switchContext](switch-context.md) | [jvm]<br>suspend fun [switchContext](switch-context.md)(context: [SynchronizationContext](../-synchronization-context/index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Switch to the specified SynchronizationContext. If this coroutine is already in the given context, this method does nothing and returns immediately. Otherwise, the behaviour is documented in [newContext](new-context.md). |
| [waitFor](wait-for.md) | [jvm]<br>suspend fun [waitFor](wait-for.md)(ticks: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>Wait for **at least** the specified amount of ticks. If the coroutine is currently backed by a non-repeating task, a new Bukkit task will be scheduled to run the specified amount of ticks later. If this coroutine is currently backed by a repeating task, the amount of ticks waited depends on the repetition resolution of the coroutine. For example, if the repetition resolution is 10 and the ticks argument is 12, it will result in a delay of 20 ticks. |
| [yield](yield.md) | [jvm]<br>suspend fun [yield](yield.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>Relinquish control for as short an amount of time as possible. That is, wait for as few ticks as possible. If this coroutine is currently backed by a non-repeating task, this will result in a task running at the next possible occasion. If this coroutine is currently backed by a repeating task, this will result in a delay for as short an amount of ticks as the repetition resolution allows. |

## Properties

| Name | Summary |
|---|---|
| [context](context.md) | [jvm]<br>open override val [context](context.md): [CoroutineContext](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html) |
| [currentTask](current-task.md) | [jvm]<br>val [currentTask](current-task.md): BukkitTask?<br>the task that is currently executing within the context of this coroutine |
| [isRepeating](is-repeating.md) | [jvm]<br>val [isRepeating](is-repeating.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>whether this coroutine is currently backed by a repeating task |
| [plugin](plugin.md) | [jvm]<br>val [plugin](plugin.md): [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md)<br>the Plugin instance to schedule the tasks bound to this coroutine |
| [scheduler](scheduler.md) | [jvm]<br>val [scheduler](scheduler.md): BukkitScheduler<br>the BukkitScheduler instance to schedule the tasks bound to this coroutine |

## Extensions

| Name | Summary |
|---|---|
| [contextAsync](../../me.racci.raccicore.utils.extensions/context-async.md) | [jvm]<br>val [BukkitSchedulerController](index.md).[contextAsync](../../me.racci.raccicore.utils.extensions/context-async.md): [SynchronizationContext](../-synchronization-context/index.md) |
| [contextSync](../../me.racci.raccicore.utils.extensions/context-sync.md) | [jvm]<br>val [BukkitSchedulerController](index.md).[contextSync](../../me.racci.raccicore.utils.extensions/context-sync.md): [SynchronizationContext](../-synchronization-context/index.md) |
| [runWithContext](../run-with-context.md) | [jvm]<br>@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")<br>~~inline suspend~~ ~~fun~~ ~~&lt;~~[T](../run-with-context.md)~~&gt;~~ [BukkitSchedulerController](index.md)~~.~~[~~runWithContext~~](../run-with-context.md)~~(~~~~context~~~~:~~ [SynchronizationContext](../-synchronization-context/index.md)~~,~~ ~~block~~~~:~~ [BukkitSchedulerController](index.md).() -&gt; [T](../run-with-context.md)~~)~~~~:~~ [~~T~~](../run-with-context.md) |
| [switchToAsync](../../me.racci.raccicore.utils.extensions/switch-to-async.md) | [jvm]<br>suspend fun [BukkitSchedulerController](index.md).[switchToAsync](../../me.racci.raccicore.utils.extensions/switch-to-async.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [switchToSync](../../me.racci.raccicore.utils.extensions/switch-to-sync.md) | [jvm]<br>suspend fun [BukkitSchedulerController](index.md).[switchToSync](../../me.racci.raccicore.utils.extensions/switch-to-sync.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [takeMaxPerTick](../../me.racci.raccicore.utils.extensions/take-max-per-tick.md) | [jvm]<br>suspend fun [BukkitSchedulerController](index.md).[takeMaxPerTick](../../me.racci.raccicore.utils.extensions/take-max-per-tick.md)(time: [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html)) |
