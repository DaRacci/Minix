//[RacciCore](../../../index.md)/[me.racci.raccicore.runnables](../index.md)/[TaskManager](index.md)

# TaskManager

[jvm]\
object [TaskManager](index.md)

## Functions

| Name | Summary |
|---|---|
| [close](close.md) | [jvm]<br>fun [close](close.md)(id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))<br>suspend fun [close](close.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md)) |
| [init](init.md) | [jvm]<br>fun [init](init.md)(task: [CoroutineTask](../-coroutine-task/index.md), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [activeTasks](active-tasks.md) | [jvm]<br>val [activeTasks](active-tasks.md): [ConcurrentHashMap](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), [CoroutineTask](../-coroutine-task/index.md)&gt;<br>The active and currently running tasks |
| [pluginTaskMap](plugin-task-map.md) | [jvm]<br>val [pluginTaskMap](plugin-task-map.md): [ConcurrentHashMap](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html)&lt;[RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md), [ArrayList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt;&gt;<br>Map for acquiring all of a plugins registered tasks |
