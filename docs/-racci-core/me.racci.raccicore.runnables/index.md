//[RacciCore](../../index.md)/[me.racci.raccicore.runnables](index.md)

# Package me.racci.raccicore.runnables

## Types

| Name | Summary |
|---|---|
| [ConstantTask](-constant-task/index.md) | [jvm]<br>@ApiStatus.Experimental<br>class [ConstantTask](-constant-task/index.md)(plugin: [RacciPlugin](../me.racci.raccicore/-racci-plugin/index.md), async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), period: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), task: CoroutineScope.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))<br>Create a new Constant running task. |
| [CoroutineTask](-coroutine-task/index.md) | [jvm]<br>class [CoroutineTask](-coroutine-task/index.md)(task: suspend Job.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html), owner: [RacciPlugin](../me.racci.raccicore/-racci-plugin/index.md), period: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |
| [KotlinRunnable](-kotlin-runnable/index.md) | [jvm]<br>abstract class [KotlinRunnable](-kotlin-runnable/index.md)(plugin: [RacciPlugin](../me.racci.raccicore/-racci-plugin/index.md), async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), repeating: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), period: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)) : BukkitRunnable |
| [KotlinTask](-kotlin-task/index.md) | [jvm]<br>abstract class [KotlinTask](-kotlin-task/index.md) |
| [RunnableManager](-runnable-manager/index.md) | [jvm]<br>object [RunnableManager](-runnable-manager/index.md) |
| [TaskManager](-task-manager/index.md) | [jvm]<br>object [TaskManager](-task-manager/index.md) |
| [TimeRunnable](-time-runnable/index.md) | [jvm]<br>class [TimeRunnable](-time-runnable/index.md)(plugin: [RacciPlugin](../me.racci.raccicore/-racci-plugin/index.md)) : [KotlinRunnable](-kotlin-runnable/index.md), [KotlinListener](../me.racci.raccicore.utils.extensions/-kotlin-listener/index.md) |

## Functions

| Name | Summary |
|---|---|
| [runnable](runnable.md) | [jvm]<br>suspend fun [RacciPlugin](../me.racci.raccicore/-racci-plugin/index.md).[runnable](runnable.md)(async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, repeating: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 0L, period: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 0L, unit: [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
