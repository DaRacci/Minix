---
title: catch
---
//[RacciCore](../../index.html)/[me.racci.raccicore.api.utils](index.html)/[catch](catch.html)



# catch



[jvm]\
inline fun &lt;[T](catch.html) : [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html), [U](catch.html) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [catch](catch.html)(err: ([T](catch.html)) -&gt; [U](catch.html), run: () -&gt; [U](catch.html)): [U](catch.html)

inline fun &lt;[T](catch.html) : [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)&gt; [catch](catch.html)(err: ([T](catch.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = { it.printStackTrace() }, run: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))

inline fun &lt;[T](catch.html) : [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html), [U](catch.html) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [catch](catch.html)(default: [U](catch.html), run: () -&gt; [U](catch.html)): [U](catch.html)




