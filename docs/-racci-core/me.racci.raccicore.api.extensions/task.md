//[RacciCore](../../index.md)/[me.racci.raccicore.api.extensions](index.md)/[task](task.md)

# task

[jvm]\
inline fun [task](task.md)(delayToRun: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 0, repeatDelay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = -1,
plugin: [RacciPlugin](../me.racci.raccicore.api.plugin/-racci-plugin/index.md), crossinline runnable: [CoroutineRunnable](../me.racci.raccicore.api.scheduler/-coroutine-runnable/index.md).()
-&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [CoroutineTask](../me.racci.raccicore.api.scheduler/-coroutine-task/index.md)

inline fun [RacciPlugin](../me.racci.raccicore.api.plugin/-racci-plugin/index.md).[task](task.md)(delayToRun: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 0,
repeatDelay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = -1, crossinline runnable: [CoroutineRunnable](../me.racci.raccicore.api.scheduler/-coroutine-runnable/index.md).()
-&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [CoroutineTask](../me.racci.raccicore.api.scheduler/-coroutine-task/index.md)

inline fun [WithPlugin](-with-plugin/index.md)&lt;*&gt;.[task](task.md)(delayToRun: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 0, repeatDelay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = -1, crossinline
runnable: [CoroutineRunnable](../me.racci.raccicore.api.scheduler/-coroutine-runnable/index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [CoroutineTask](../me.racci.raccicore.api.scheduler/-coroutine-task/index.md)

inline fun [task](task.md)(delayToRun: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), repeatDelay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = -1,
async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), plugin: [RacciPlugin](../me.racci.raccicore.api.plugin/-racci-plugin/index.md), crossinline runnable: [CoroutineRunnable](../me.racci.raccicore.api.scheduler/-coroutine-runnable/index.md)
.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [CoroutineTask](../me.racci.raccicore.api.scheduler/-coroutine-task/index.md)
