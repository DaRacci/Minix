//[RacciCore](../../index.md)/[me.racci.raccicore.skedule](index.md)/[skeduleAsync](skedule-async.md)

# skeduleAsync

[jvm]\

@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")

~~fun~~ [~~skeduleAsync~~](skedule-async.md)~~(~~~~plugin~~~~:~~ [RacciPlugin](../me.racci.raccicore/-racci-plugin/index.md)~~,~~ ~~block~~~~:~~ suspend [BukkitSchedulerController](-bukkit-scheduler-controller/index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)~~)~~~~:~~ [CoroutineTask](-coroutine-task/index.md)

Sugar functions to allow for easier creation of coroutines.

For example, before:

Bukkit.getScheduler().schedule(myPlugin, SynchronizationContext.ASYNC) {\
    //...\
}

After:

skeduleAsync(myPlugin) {\
    //...\
}
