//[RacciCore](../../index.md)/[me.racci.raccicore.api.utils](index.md)/[catch](catch.md)

# catch

[jvm]\
inline fun &lt;[T](catch.md) : [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html), [U](catch.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [catch](catch.md)(err: ([T](catch.md)) -&gt; [U](catch.md), run: () -&gt; [U](catch.md)): [U](catch.md)

inline fun &lt;[T](catch.md) : [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)&gt; [catch](catch.md)(err: ([T](catch.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = { it.printStackTrace() }, run: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))

inline fun &lt;[T](catch.md) : [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html), [U](catch.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [catch](catch.md)(default: [U](catch.md), run: () -&gt; [U](catch.md)): [U](catch.md)
