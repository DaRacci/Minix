//[RacciCore](../../../index.md)/[me.racci.raccicore.runnables](../index.md)/[TimeRunnable](index.md)

# TimeRunnable

[jvm]\
class [TimeRunnable](index.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md)) : [KotlinRunnable](../-kotlin-runnable/index.md), [KotlinListener](../../me.racci.raccicore.utils.extensions/-kotlin-listener/index.md)

## Functions

| Name | Summary |
|---|---|
| [cancel](index.md#-1778178423%2FFunctions%2F-519281799) | [jvm]<br>open fun [cancel](index.md#-1778178423%2FFunctions%2F-519281799)() |
| [getTaskId](index.md#796542529%2FFunctions%2F-519281799) | [jvm]<br>open fun [getTaskId](index.md#796542529%2FFunctions%2F-519281799)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [isCancelled](index.md#-1530665456%2FFunctions%2F-519281799) | [jvm]<br>open fun [isCancelled](index.md#-1530665456%2FFunctions%2F-519281799)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [onWorldLoad](on-world-load.md) | [jvm]<br>suspend fun [onWorldLoad](on-world-load.md)(event: WorldLoadEvent) |
| [run](run.md) | [jvm]<br>open override fun [run](run.md)() |
| [runTask](index.md#-1602658785%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>open fun [runTask](index.md#-1602658785%2FFunctions%2F-519281799)(@NotNullp0: @NotNullPlugin): @NotNullBukkitTask |
| [runTaskAsynchronously](index.md#1779209546%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>open fun [runTaskAsynchronously](index.md#1779209546%2FFunctions%2F-519281799)(@NotNullp0: @NotNullPlugin): @NotNullBukkitTask |
| [runTaskLater](index.md#1812878393%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>open fun [runTaskLater](index.md#1812878393%2FFunctions%2F-519281799)(@NotNullp0: @NotNullPlugin, p1: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): @NotNullBukkitTask |
| [runTaskLaterAsynchronously](index.md#350548708%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>open fun [runTaskLaterAsynchronously](index.md#350548708%2FFunctions%2F-519281799)(@NotNullp0: @NotNullPlugin, p1: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): @NotNullBukkitTask |
| [runTaskTimer](index.md#841103738%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>open fun [runTaskTimer](index.md#841103738%2FFunctions%2F-519281799)(@NotNullp0: @NotNullPlugin, p1: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), p2: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): @NotNullBukkitTask |
| [runTaskTimerAsynchronously](index.md#479702885%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>open fun [runTaskTimerAsynchronously](index.md#479702885%2FFunctions%2F-519281799)(@NotNullp0: @NotNullPlugin, p1: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), p2: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): @NotNullBukkitTask |
| [start](../-kotlin-runnable/start.md) | [jvm]<br>fun [start](../-kotlin-runnable/start.md)() |

## Properties

| Name | Summary |
|---|---|
| [async](../-kotlin-runnable/async.md) | [jvm]<br>val [async](../-kotlin-runnable/async.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false |
| [delay](../-kotlin-runnable/delay.md) | [jvm]<br>val [delay](../-kotlin-runnable/delay.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 0L |
| [period](../-kotlin-runnable/period.md) | [jvm]<br>val [period](../-kotlin-runnable/period.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 20L |
| [plugin](../-kotlin-runnable/plugin.md) | [jvm]<br>val [plugin](../-kotlin-runnable/plugin.md): [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md) |
| [repeating](../-kotlin-runnable/repeating.md) | [jvm]<br>val [repeating](../-kotlin-runnable/repeating.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false |
