//[RacciCore](../../../index.md)/[me.racci.raccicore.api.utils.collections](../index.md)/[ObservableMutableListIterator](index.md)

# ObservableMutableListIterator

[jvm]\
class [ObservableMutableListIterator](index.md)&lt;[T](index.md)&gt;(iterator: [MutableListIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list-iterator/index.html)&lt;[T](index.md)&gt;, runListeners: [ObservableListener](../index.md#1056729540%2FClasslikes%2F-1216412040)) : [MutableListIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list-iterator/index.html)&lt;[T](index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [add](add.md) | [jvm]<br>open override fun [add](add.md)(element: [T](index.md)) |
| [forEachRemaining](../-observable-mutable-iterator/index.md#-511368593%2FFunctions%2F-1216412040) | [jvm]<br>open fun [forEachRemaining](../-observable-mutable-iterator/index.md#-511368593%2FFunctions%2F-1216412040)(p0: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;in [T](index.md)&gt;) |
| [hasNext](index.md#-259863468%2FFunctions%2F-1216412040) | [jvm]<br>open operator override fun [hasNext](index.md#-259863468%2FFunctions%2F-1216412040)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hasPrevious](index.md#-1472436248%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [hasPrevious](index.md#-1472436248%2FFunctions%2F-1216412040)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [next](index.md#-496733690%2FFunctions%2F-1216412040) | [jvm]<br>open operator override fun [next](index.md#-496733690%2FFunctions%2F-1216412040)(): [T](index.md) |
| [nextIndex](index.md#171882682%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [nextIndex](index.md#171882682%2FFunctions%2F-1216412040)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [previous](index.md#1653527402%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [previous](index.md#1653527402%2FFunctions%2F-1216412040)(): [T](index.md) |
| [previousIndex](index.md#344902590%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [previousIndex](index.md#344902590%2FFunctions%2F-1216412040)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [remove](remove.md) | [jvm]<br>open override fun [remove](remove.md)() |
| [set](set.md) | [jvm]<br>open override fun [set](set.md)(element: [T](index.md)) |
