//[RacciCore](../../index.md)/[me.racci.raccicore.utils.extensions](index.md)/[taskAsync](task-async.md)

# taskAsync

[jvm]\
inline fun [taskAsync](task-async.md)(delayToRun: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 0, repeatDelay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = -1, plugin: Plugin, crossinline runnable: BukkitRunnable.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): @NotNullBukkitTask

inline fun Plugin.[taskAsync](task-async.md)(delayToRun: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 0, repeatDelay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = -1, crossinline runnable: BukkitRunnable.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): @NotNullBukkitTask

inline fun [WithPlugin](-with-plugin/index.md)&lt;*&gt;.[taskAsync](task-async.md)(delayToRun: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 0, repeatDelay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = -1, crossinline runnable: BukkitRunnable.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): @NotNullBukkitTask
