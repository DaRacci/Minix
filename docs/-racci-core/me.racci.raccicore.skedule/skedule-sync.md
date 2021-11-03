//[RacciCore](../../index.md)/[me.racci.raccicore.skedule](index.md)/[skeduleSync](skedule-sync.md)

# skeduleSync

[jvm]\

@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")

~~fun~~ [~~skeduleSync~~](skedule-sync.md)~~(~~~~plugin~~~~:~~ [RacciPlugin](../me.racci.raccicore/-racci-plugin/index.md)~~,~~ ~~block~~~~:~~ suspend [BukkitSchedulerController](-bukkit-scheduler-controller/index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)~~)~~~~:~~ [CoroutineTask](-coroutine-task/index.md)

Sugar functions to allow for easier creation of coroutines.

For example, before:

Bukkit.getScheduler().schedule(myPlugin) {\
    //...\
}

After:

skeduleSync(myPlugin) {\
    //...\
}
