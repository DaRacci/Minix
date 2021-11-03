//[RacciCore](../../index.md)/[me.racci.raccicore.skedule](index.md)/[schedule](schedule.md)

# schedule

[jvm]\

@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")

~~fun~~ [RacciPlugin](../me.racci.raccicore/-racci-plugin/index.md)~~.~~[~~schedule~~](schedule.md)~~(~~~~initialContext~~~~:~~ [SynchronizationContext](-synchronization-context/index.md) ~~= SynchronizationContext.SYNC~~~~,~~ ~~co~~~~:~~ suspend [BukkitSchedulerController](-bukkit-scheduler-controller/index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)~~)~~~~:~~ [CoroutineTask](-coroutine-task/index.md)

[jvm]\

@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")

~~fun~~ BukkitScheduler~~.~~[~~schedule~~](schedule.md)~~(~~~~plugin~~~~:~~ [RacciPlugin](../me.racci.raccicore/-racci-plugin/index.md)~~,~~ ~~initialContext~~~~:~~ [SynchronizationContext](-synchronization-context/index.md) ~~= SynchronizationContext.SYNC~~~~,~~ ~~co~~~~:~~ suspend [BukkitSchedulerController](-bukkit-scheduler-controller/index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)~~)~~~~:~~ [CoroutineTask](-coroutine-task/index.md)

Schedule a coroutine with the Bukkit Scheduler.

#### Receiver

The BukkitScheduler instance to use for scheduling tasks.

## See also

jvm

| | |
|---|---|
| [me.racci.raccicore.skedule.SynchronizationContext](-synchronization-context/index.md) |  |

## Parameters

jvm

| | |
|---|---|
| plugin | The Plugin instance to use for scheduling tasks. |
| initialContext | The initial synchronization context to start off the coroutine with. See [SynchronizationContext](-synchronization-context/index.md). |
