//[RacciCore](../../../index.md)/[me.racci.raccicore.runnables](../index.md)/[KotlinRunnable](index.md)

# KotlinRunnable

[jvm]\
abstract class [KotlinRunnable](index.md)(plugin: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md), async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), repeating: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), period: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)) : BukkitRunnable

## Functions

| Name | Summary |
|---|---|
| [cancel](../-time-runnable/index.md#-1778178423%2FFunctions%2F-519281799) | [jvm]<br>open fun [cancel](../-time-runnable/index.md#-1778178423%2FFunctions%2F-519281799)() |
| [getTaskId](../-time-runnable/index.md#796542529%2FFunctions%2F-519281799) | [jvm]<br>open fun [getTaskId](../-time-runnable/index.md#796542529%2FFunctions%2F-519281799)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [isCancelled](../-time-runnable/index.md#-1530665456%2FFunctions%2F-519281799) | [jvm]<br>open fun [isCancelled](../-time-runnable/index.md#-1530665456%2FFunctions%2F-519281799)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [run](index.md#-853624561%2FFunctions%2F-519281799) | [jvm]<br>abstract fun [run](index.md#-853624561%2FFunctions%2F-519281799)() |
| [runTask](../-time-runnable/index.md#-1602658785%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>open fun [runTask](../-time-runnable/index.md#-1602658785%2FFunctions%2F-519281799)(@NotNullp0: @NotNullPlugin): @NotNullBukkitTask |
| [runTaskAsynchronously](../-time-runnable/index.md#1779209546%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>open fun [runTaskAsynchronously](../-time-runnable/index.md#1779209546%2FFunctions%2F-519281799)(@NotNullp0: @NotNullPlugin): @NotNullBukkitTask |
| [runTaskLater](../-time-runnable/index.md#1812878393%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>open fun [runTaskLater](../-time-runnable/index.md#1812878393%2FFunctions%2F-519281799)(@NotNullp0: @NotNullPlugin, p1: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): @NotNullBukkitTask |
| [runTaskLaterAsynchronously](../-time-runnable/index.md#350548708%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>open fun [runTaskLaterAsynchronously](../-time-runnable/index.md#350548708%2FFunctions%2F-519281799)(@NotNullp0: @NotNullPlugin, p1: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): @NotNullBukkitTask |
| [runTaskTimer](../-time-runnable/index.md#841103738%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>open fun [runTaskTimer](../-time-runnable/index.md#841103738%2FFunctions%2F-519281799)(@NotNullp0: @NotNullPlugin, p1: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), p2: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): @NotNullBukkitTask |
| [runTaskTimerAsynchronously](../-time-runnable/index.md#479702885%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>open fun [runTaskTimerAsynchronously](../-time-runnable/index.md#479702885%2FFunctions%2F-519281799)(@NotNullp0: @NotNullPlugin, p1: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), p2: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): @NotNullBukkitTask |
| [start](start.md) | [jvm]<br>fun [start](start.md)() |

## Properties

| Name | Summary |
|---|---|
| [async](async.md) | [jvm]<br>val [async](async.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false |
| [delay](delay.md) | [jvm]<br>val [delay](delay.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 0L |
| [period](period.md) | [jvm]<br>val [period](period.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 20L |
| [plugin](plugin.md) | [jvm]<br>val [plugin](plugin.md): [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md) |
| [repeating](repeating.md) | [jvm]<br>val [repeating](repeating.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false |

## Inheritors

| Name |
|---|
| [TimeRunnable](../-time-runnable/index.md) |
