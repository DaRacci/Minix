---
title: me.racci.raccicore.api.utils
---
//[RacciCore](../../index.html)/[me.racci.raccicore.api.utils](index.html)



# Package me.racci.raccicore.api.utils



## Types


| Name | Summary |
|---|---|
| [TimeConversionUtils](-time-conversion-utils/index.html) | [jvm]<br>object [TimeConversionUtils](-time-conversion-utils/index.html) |
| [UpdateChecker](-update-checker/index.html) | [jvm]<br>class [UpdateChecker](-update-checker/index.html)(plugin: [RacciPlugin](../me.racci.raccicore.api.plugin/-racci-plugin/index.html)) |


## Functions


| Name | Summary |
|---|---|
| [catch](catch.html) | [jvm]<br>inline fun &lt;[T](catch.html) : [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html), [U](catch.html) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [catch](catch.html)(default: [U](catch.html), run: () -&gt; [U](catch.html)): [U](catch.html)<br>inline fun &lt;[T](catch.html) : [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html), [U](catch.html) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [catch](catch.html)(err: ([T](catch.html)) -&gt; [U](catch.html), run: () -&gt; [U](catch.html)): [U](catch.html)<br>inline fun &lt;[T](catch.html) : [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)&gt; [catch](catch.html)(err: ([T](catch.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = { it.printStackTrace() }, run: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [catchAndReturn](catch-and-return.html) | [jvm]<br>inline fun &lt;[T](catch-and-return.html) : [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html), [R](catch-and-return.html)&gt; [catchAndReturn](catch-and-return.html)(err: ([T](catch-and-return.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {it.printStackTrace()}, run: () -&gt; [R](catch-and-return.html)): [R](catch-and-return.html)? |
| [classConstructor](class-constructor.html) | [jvm]<br>fun &lt;[T](class-constructor.html)&gt; [classConstructor](class-constructor.html)(constructor: [Constructor](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Constructor.html)&lt;[T](class-constructor.html)&gt;, vararg args: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [T](class-constructor.html) |
| [exists](exists.html) | [jvm]<br>fun [exists](exists.html)(className: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if a class exists |
| [listen](listen.html) | [jvm]<br>inline fun &lt;[T](listen.html) : Event&gt; [RacciPlugin](../me.racci.raccicore.api.plugin/-racci-plugin/index.html).[listen](listen.html)(priority: EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, crossinline callback: ([T](listen.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [not](not.html) | [jvm]<br>fun &lt;[T](not.html)&gt; [T](not.html).[not](not.html)(other: [T](not.html)): [T](not.html)? |
| [notIn](not-in.html) | [jvm]<br>fun &lt;[T](not-in.html)&gt; [T](not-in.html).[notIn](not-in.html)(container: [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)&lt;[T](not-in.html)&gt;): [T](not-in.html)? |
| [now](now.html) | [jvm]<br>fun [now](now.html)(): Instant |
| [readInstanceProperty](read-instance-property.html) | [jvm]<br>fun &lt;[R](read-instance-property.html)&gt; [readInstanceProperty](read-instance-property.html)(instance: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), propertyName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), ignoreCase: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false): [R](read-instance-property.html) |
| [takeMaxPerTick](take-max-per-tick.html) | [jvm]<br>suspend fun [WithPlugin](../me.racci.raccicore.api.extensions/-with-plugin/index.html)&lt;*&gt;.[takeMaxPerTick](take-max-per-tick.html)(time: [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html))<br>suspend fun [RacciPlugin](../me.racci.raccicore.api.plugin/-racci-plugin/index.html).[takeMaxPerTick](take-max-per-tick.html)(time: [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html)) |
| [tryCast](try-cast.html) | [jvm]<br>inline fun &lt;[T](try-cast.html)&gt; [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?.[tryCast](try-cast.html)(block: [T](try-cast.html).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |


## Properties


| Name | Summary |
|---|---|
| [uuid](uuid.html) | [jvm]<br>val Player.[uuid](uuid.html): [UUID](https://docs.oracle.com/javase/8/docs/api/java/util/UUID.html) |

