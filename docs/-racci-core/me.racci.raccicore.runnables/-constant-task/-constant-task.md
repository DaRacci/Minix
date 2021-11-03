//[RacciCore](../../../index.md)/[me.racci.raccicore.runnables](../index.md)/[ConstantTask](index.md)/[ConstantTask](-constant-task.md)

# ConstantTask

[jvm]\
fun [ConstantTask](-constant-task.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md), async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), period: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), task: CoroutineScope.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))

## Parameters

jvm

| | |
|---|---|
| plugin | the plugin which owns the task |
| async | if the task will be executed on the main Bukkit thread or not |
| period | how often the task is run in ticks (Second/1000) |
| task | the unit which will be run during the task |
