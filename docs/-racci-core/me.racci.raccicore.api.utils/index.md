//[RacciCore](../../index.md)/[me.racci.raccicore.api.utils](index.md)

# Package me.racci.raccicore.api.utils

## Types

| Name | Summary |
|---|---|
| [TimeConversionUtils](-time-conversion-utils/index.md) | [jvm]<br>object [TimeConversionUtils](-time-conversion-utils/index.md) |
| [UpdateChecker](-update-checker/index.md) | [jvm]<br>class [UpdateChecker](-update-checker/index.md)(plugin: [RacciPlugin](../me.racci.raccicore.api.plugin/-racci-plugin/index.md)) |

## Functions

| Name | Summary |
|---|---|
| [catch](catch.md) | [jvm]<br>inline fun &lt;[T](catch.md) : [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html), [U](catch.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [catch](catch.md)(default: [U](catch.md), run: () -&gt; [U](catch.md)): [U](catch.md)<br>inline fun &lt;[T](catch.md) : [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html), [U](catch.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [catch](catch.md)(err: ([T](catch.md)) -&gt; [U](catch.md), run: () -&gt; [U](catch.md)): [U](catch.md)<br>inline fun &lt;[T](catch.md) : [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)&gt; [catch](catch.md)(err: ([T](catch.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = { it.printStackTrace() }, run: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [catchAndReturn](catch-and-return.md) | [jvm]<br>inline fun &lt;[T](catch-and-return.md) : [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html), [R](catch-and-return.md)&gt; [catchAndReturn](catch-and-return.md)(err: ([T](catch-and-return.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {it.printStackTrace()}, run: () -&gt; [R](catch-and-return.md)): [R](catch-and-return.md)? |
| [classConstructor](class-constructor.md) | [jvm]<br>fun &lt;[T](class-constructor.md)&gt; [classConstructor](class-constructor.md)(constructor: [Constructor](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Constructor.html)&lt;[T](class-constructor.md)&gt;, vararg args: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [T](class-constructor.md) |
| [exists](exists.md) | [jvm]<br>fun [exists](exists.md)(className: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if a class exists |
| [listen](listen.md) | [jvm]<br>inline fun &lt;[T](listen.md) : Event&gt; [RacciPlugin](../me.racci.raccicore.api.plugin/-racci-plugin/index.md).[listen](listen.md)(priority: EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, crossinline callback: ([T](listen.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [not](not.md) | [jvm]<br>fun &lt;[T](not.md)&gt; [T](not.md).[not](not.md)(other: [T](not.md)): [T](not.md)? |
| [notIn](not-in.md) | [jvm]<br>fun &lt;[T](not-in.md)&gt; [T](not-in.md).[notIn](not-in.md)(container: [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)&lt;[T](not-in.md)&gt;): [T](not-in.md)? |
| [now](now.md) | [jvm]<br>fun [now](now.md)(): Instant |
| [readInstanceProperty](read-instance-property.md) | [jvm]<br>fun &lt;[R](read-instance-property.md)&gt; [readInstanceProperty](read-instance-property.md)(instance: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), propertyName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), ignoreCase: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false): [R](read-instance-property.md) |
| [takeMaxPerTick](take-max-per-tick.md) | [jvm]<br>suspend fun [WithPlugin](../me.racci.raccicore.api.extensions/-with-plugin/index.md)&lt;*&gt;.[takeMaxPerTick](take-max-per-tick.md)(time: [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html))<br>suspend fun [RacciPlugin](../me.racci.raccicore.api.plugin/-racci-plugin/index.md).[takeMaxPerTick](take-max-per-tick.md)(time: [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html)) |
| [tryCast](try-cast.md) | [jvm]<br>inline fun &lt;[T](try-cast.md)&gt; [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?.[tryCast](try-cast.md)(block: [T](try-cast.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

## Properties

| Name | Summary |
|---|---|
| [uuid](uuid.md) | [jvm]<br>val Player.[uuid](uuid.md): [UUID](https://docs.oracle.com/javase/8/docs/api/java/util/UUID.html) |
