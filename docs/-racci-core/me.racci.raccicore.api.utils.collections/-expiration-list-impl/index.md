//[RacciCore](../../../index.md)/[me.racci.raccicore.api.utils.collections](../index.md)/[ExpirationListImpl](index.md)

# ExpirationListImpl

[jvm]\
class [ExpirationListImpl](index.md)&lt;[E](index.md)&gt;(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md)) : [ExpirationList](../-expiration-list/index.md)&lt;[E](index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [add](add.md) | [jvm]<br>open override fun [add](add.md)(element: [E](index.md), expireTime: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), onExpire: [OnExpireCallback](../index.md#-1395177404%2FClasslikes%2F-1216412040)&lt;[E](index.md)&gt;?)<br>Add the element to the list with an expiration time. |
| [addFirst](add-first.md) | [jvm]<br>open override fun [addFirst](add-first.md)(element: [E](index.md), expireTime: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), onExpire: [OnExpireCallback](../index.md#-1395177404%2FClasslikes%2F-1216412040)&lt;[E](index.md)&gt;?)<br>Add the element in the start of list with an expiration time. |
| [clear](clear.md) | [jvm]<br>open override fun [clear](clear.md)()<br>Removes all elements from this list. |
| [contains](contains.md) | [jvm]<br>open operator override fun [contains](contains.md)(element: [E](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if the specified element is contained in this list. |
| [first](first.md) | [jvm]<br>open override fun [first](first.md)(): [E](index.md)?<br>Return the first element in the list or null if the list is empty |
| [forEach](../../me.racci.raccicore.api.utils.minecraft/-pos-range/index.md#1532301601%2FFunctions%2F-1216412040) | [jvm]<br>open fun [forEach](../../me.racci.raccicore.api.utils.minecraft/-pos-range/index.md#1532301601%2FFunctions%2F-1216412040)(p0: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;in [E](index.md)&gt;) |
| [get](get.md) | [jvm]<br>open operator override fun [get](get.md)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [E](index.md)?<br>Returns the element at the specified index in the list. |
| [indexOf](index-of.md) | [jvm]<br>open override fun [indexOf](index-of.md)(element: [E](index.md)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns the index of the first occurrence of the e element in the list, or -1 if the specified element is not contained in the list. |
| [isEmpty](is-empty.md) | [jvm]<br>open override fun [isEmpty](is-empty.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Returns true if the list contains no elements, false otherwise. |
| [iterator](iterator.md) | [jvm]<br>open operator override fun [iterator](iterator.md)(): [MutableIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterator/index.html)&lt;[E](index.md)&gt; |
| [last](last.md) | [jvm]<br>open override fun [last](last.md)(): [E](index.md)?<br>Return the last element in the list or null if the list is empty |
| [missingTime](missing-time.md) | [jvm]<br>open override fun [missingTime](missing-time.md)(element: [E](index.md)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?<br>Returns the time missing expiring or null if the list don't contain the element |
| [remove](remove.md) | [jvm]<br>open override fun [remove](remove.md)(element: [E](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Removes an element from the list |
| [removeAt](remove-at.md) | [jvm]<br>open override fun [removeAt](remove-at.md)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [E](index.md)?<br>Removes an element at the specified [index](remove-at.md) from the list. |
| [removeFirst](remove-first.md) | [jvm]<br>open override fun [removeFirst](remove-first.md)(): [E](index.md)?<br>Removes the first element from the list. |
| [removeLast](remove-last.md) | [jvm]<br>open override fun [removeLast](remove-last.md)(): [E](index.md)?<br>Removes the last element from the list. |
| [spliterator](../../me.racci.raccicore.api.utils.minecraft/-pos-range/index.md#-1387152138%2FFunctions%2F-1216412040) | [jvm]<br>open fun [spliterator](../../me.racci.raccicore.api.utils.minecraft/-pos-range/index.md#-1387152138%2FFunctions%2F-1216412040)(): [Spliterator](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)&lt;[E](index.md)&gt; |

## Properties

| Name | Summary |
|---|---|
| [_size](_size.md) | [jvm]<br>var [_size](_size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |
| [size](size.md) | [jvm]<br>open override val [size](size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns the count of elements in this list. |
