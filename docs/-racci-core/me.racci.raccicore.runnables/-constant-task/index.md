//[RacciCore](../../../index.md)/[me.racci.raccicore.runnables](../index.md)/[ConstantTask](index.md)

# ConstantTask

[jvm]\
@ApiStatus.Experimental

class [ConstantTask](index.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md), async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), period: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), task: CoroutineScope.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))

Create a new Constant running task.

## Parameters

jvm

| | |
|---|---|
| plugin | the plugin which owns the task |
| async | if the task will be executed on the main Bukkit thread or not |
| period | how often the task is run in ticks (Second/1000) |
| task | the unit which will be run during the task |

## Constructors

| | |
|---|---|
| [ConstantTask](-constant-task.md) | [jvm]<br>fun [ConstantTask](-constant-task.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md), async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), period: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), task: CoroutineScope.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |

## Functions

| Name | Summary |
|---|---|
| [close](close.md) | [jvm]<br>suspend fun [close](close.md)() |
| [init](init.md) | [jvm]<br>fun [init](init.md)() |

## Properties

| Name | Summary |
|---|---|
| [async](async.md) | [jvm]<br>val [async](async.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [period](period.md) | [jvm]<br>val [period](period.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [plugin](plugin.md) | [jvm]<br>val [plugin](plugin.md): [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md) |
| [task](task.md) | [jvm]<br>val [task](task.md): CoroutineScope.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
