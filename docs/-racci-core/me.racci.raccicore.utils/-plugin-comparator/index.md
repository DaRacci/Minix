//[RacciCore](../../../index.md)/[me.racci.raccicore.utils](../index.md)/[PluginComparator](index.md)

# PluginComparator

[jvm]\
object [PluginComparator](index.md) : [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)&lt;Plugin&gt;

## Functions

| Name | Summary |
|---|---|
| [compare](compare.md) | [jvm]<br>open override fun [compare](compare.md)(p0: Plugin, p1: Plugin): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [reversed](../-player-comparator/index.md#208665987%2FFunctions%2F-519281799) | [jvm]<br>open fun [reversed](../-player-comparator/index.md#208665987%2FFunctions%2F-519281799)(): [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)&lt;Plugin&gt; |
| [thenComparing](index.md#-892172736%2FFunctions%2F-519281799) | [jvm]<br>open fun [thenComparing](index.md#-892172736%2FFunctions%2F-519281799)(p0: [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)&lt;in Plugin&gt;): [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)&lt;Plugin&gt;<br>open fun &lt;[U](index.md#343173329%2FFunctions%2F-519281799) : [Comparable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparable/index.html)&lt;[U](index.md#343173329%2FFunctions%2F-519281799)&gt;&gt; [thenComparing](index.md#343173329%2FFunctions%2F-519281799)(p0: [Function](https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html)&lt;in Plugin, out [U](index.md#343173329%2FFunctions%2F-519281799)&gt;): [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)&lt;Plugin&gt;<br>open fun &lt;[U](index.md#1586812445%2FFunctions%2F-519281799) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [thenComparing](index.md#1586812445%2FFunctions%2F-519281799)(p0: [Function](https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html)&lt;in Plugin, out [U](index.md#1586812445%2FFunctions%2F-519281799)&gt;, p1: [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)&lt;in [U](index.md#1586812445%2FFunctions%2F-519281799)&gt;): [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)&lt;Plugin&gt; |
| [thenComparingDouble](index.md#-176355601%2FFunctions%2F-519281799) | [jvm]<br>open fun [thenComparingDouble](index.md#-176355601%2FFunctions%2F-519281799)(p0: [ToDoubleFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/ToDoubleFunction.html)&lt;in Plugin&gt;): [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)&lt;Plugin&gt; |
| [thenComparingInt](index.md#-1174416821%2FFunctions%2F-519281799) | [jvm]<br>open fun [thenComparingInt](index.md#-1174416821%2FFunctions%2F-519281799)(p0: [ToIntFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/ToIntFunction.html)&lt;in Plugin&gt;): [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)&lt;Plugin&gt; |
| [thenComparingLong](index.md#-918281905%2FFunctions%2F-519281799) | [jvm]<br>open fun [thenComparingLong](index.md#-918281905%2FFunctions%2F-519281799)(p0: [ToLongFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/ToLongFunction.html)&lt;in Plugin&gt;): [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)&lt;Plugin&gt; |