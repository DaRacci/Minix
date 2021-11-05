//[RacciCore](../../../index.md)/[me.racci.raccicore.runnables](../index.md)/[CoroutineTask](index.md)

# CoroutineTask

[jvm]\
class [CoroutineTask](index.md)(task: suspend Job.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html), owner: [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md), period: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html))

## Functions

| Name | Summary |
|---|---|
| [cancel](cancel.md) | [jvm]<br>fun [cancel](cancel.md)() |
| [init](init.md) | [jvm]<br>fun [init](init.md)() |

## Properties

| Name | Summary |
|---|---|
| [async](async.md) | [jvm]<br>val [async](async.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [cancelled](cancelled.md) | [jvm]<br>var [cancelled](cancelled.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false |
| [job](job.md) | [jvm]<br>val [job](job.md): Job? = null |
| [owner](owner.md) | [jvm]<br>val [owner](owner.md): [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md) |
| [period](period.md) | [jvm]<br>val [period](period.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
