//[RacciCore](../../../index.md)/[me.racci.raccicore.skedule](../index.md)/[BukkitDispatcher](index.md)

# BukkitDispatcher

[jvm]\
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")

~~class~~ [~~BukkitDispatcher~~](index.md)~~(~~~~plugin~~~~:~~ [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md)~~,~~ ~~async~~~~:~~ [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)~~)~~ ~~:~~ ~~CoroutineDispatcher~~~~,~~ ~~Delay~~

## Functions

| Name | Summary |
|---|---|
| [delay](index.md#328100086%2FFunctions%2F-519281799) | [jvm]<br>open suspend fun [delay](index.md#328100086%2FFunctions%2F-519281799)(time: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)) |
| [dispatch](dispatch.md) | [jvm]<br>open override fun [dispatch](dispatch.md)(context: [CoroutineContext](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html), block: Runnable) |
| [dispatchYield](index.md#-1925042348%2FFunctions%2F-519281799) | [jvm]<br>open fun [dispatchYield](index.md#-1925042348%2FFunctions%2F-519281799)(context: [CoroutineContext](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html), block: Runnable) |
| [fold](index.md#466442070%2FFunctions%2F-519281799) | [jvm]<br>open override fun &lt;[R](index.md#466442070%2FFunctions%2F-519281799)&gt; [fold](index.md#466442070%2FFunctions%2F-519281799)(initial: [R](index.md#466442070%2FFunctions%2F-519281799), operation: ([R](index.md#466442070%2FFunctions%2F-519281799), [CoroutineContext.Element](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/-element/index.html)) -&gt; [R](index.md#466442070%2FFunctions%2F-519281799)): [R](index.md#466442070%2FFunctions%2F-519281799) |
| [get](index.md#232613081%2FFunctions%2F-519281799) | [jvm]<br>open operator override fun &lt;[E](index.md#232613081%2FFunctions%2F-519281799) : [CoroutineContext.Element](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/-element/index.html)&gt; [get](index.md#232613081%2FFunctions%2F-519281799)(key: [CoroutineContext.Key](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/-key/index.html)&lt;[E](index.md#232613081%2FFunctions%2F-519281799)&gt;): [E](index.md#232613081%2FFunctions%2F-519281799)? |
| [interceptContinuation](index.md#-1798799910%2FFunctions%2F-519281799) | [jvm]<br>open override fun &lt;[T](index.md#-1798799910%2FFunctions%2F-519281799)&gt; [interceptContinuation](index.md#-1798799910%2FFunctions%2F-519281799)(continuation: [Continuation](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-continuation/index.html)&lt;[T](index.md#-1798799910%2FFunctions%2F-519281799)&gt;): [Continuation](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-continuation/index.html)&lt;[T](index.md#-1798799910%2FFunctions%2F-519281799)&gt; |
| [invokeOnTimeout](index.md#-1554827471%2FFunctions%2F-519281799) | [jvm]<br>open fun [invokeOnTimeout](index.md#-1554827471%2FFunctions%2F-519281799)(timeMillis: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), block: Runnable, context: [CoroutineContext](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html)): DisposableHandle |
| [isDispatchNeeded](index.md#-381715142%2FFunctions%2F-519281799) | [jvm]<br>open fun [isDispatchNeeded](index.md#-381715142%2FFunctions%2F-519281799)(context: [CoroutineContext](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [minusKey](index.md#-1830620809%2FFunctions%2F-519281799) | [jvm]<br>open override fun [minusKey](index.md#-1830620809%2FFunctions%2F-519281799)(key: [CoroutineContext.Key](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/-key/index.html)&lt;*&gt;): [CoroutineContext](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html) |
| [plus](index.md#1141296693%2FFunctions%2F-519281799) | [jvm]<br>open operator fun [plus](index.md#1141296693%2FFunctions%2F-519281799)(context: [CoroutineContext](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html)): [CoroutineContext](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html)<br>~~operator~~ ~~fun~~ [~~plus~~](index.md#1709458771%2FFunctions%2F-519281799)~~(~~~~other~~~~:~~ CoroutineDispatcher~~)~~~~:~~ CoroutineDispatcher |
| [releaseInterceptedContinuation](index.md#-1994945966%2FFunctions%2F-519281799) | [jvm]<br>open override fun [releaseInterceptedContinuation](index.md#-1994945966%2FFunctions%2F-519281799)(continuation: [Continuation](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-continuation/index.html)&lt;*&gt;) |
| [scheduleResumeAfterDelay](schedule-resume-after-delay.md) | [jvm]<br>open override fun [scheduleResumeAfterDelay](schedule-resume-after-delay.md)(timeMillis: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), continuation: CancellableContinuation&lt;[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;) |
| [toString](index.md#883294532%2FFunctions%2F-519281799) | [jvm]<br>open override fun [toString](index.md#883294532%2FFunctions%2F-519281799)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

## Properties

| Name | Summary |
|---|---|
| [async](async.md) | [jvm]<br>val [async](async.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false |
| [key](index.md#-1762250447%2FProperties%2F-519281799) | [jvm]<br>open override val [key](index.md#-1762250447%2FProperties%2F-519281799): [CoroutineContext.Key](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/-key/index.html)&lt;*&gt; |
| [plugin](plugin.md) | [jvm]<br>val [plugin](plugin.md): [RacciPlugin](../../me.racci.raccicore/-racci-plugin/index.md) |
