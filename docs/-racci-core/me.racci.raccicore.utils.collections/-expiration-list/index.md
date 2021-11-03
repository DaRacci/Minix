//[RacciCore](../../../index.md)/[me.racci.raccicore.utils.collections](../index.md)/[ExpirationList](index.md)

# ExpirationList

[jvm]\
interface [ExpirationList](index.md)&lt;[E](index.md)&gt; : [MutableIterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterable/index.html)&lt;[E](index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [add](add.md) | [jvm]<br>abstract fun [add](add.md)(element: [E](index.md), expireTime: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), onExpire: [OnExpireCallback](../index.md#1412320920%2FClasslikes%2F-519281799)&lt;[E](index.md)&gt;? = null)<br>Add the element to the list with an expiration time. |
| [addFirst](add-first.md) | [jvm]<br>abstract fun [addFirst](add-first.md)(element: [E](index.md), expireTime: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), onExpire: [OnExpireCallback](../index.md#1412320920%2FClasslikes%2F-519281799)&lt;[E](index.md)&gt;? = null)<br>Add the element in the start of list with an expiration time. |
| [clear](clear.md) | [jvm]<br>abstract fun [clear](clear.md)()<br>Removes all elements from this list. |
| [contains](contains.md) | [jvm]<br>abstract operator fun [contains](contains.md)(element: [E](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if the specified element is contained in this list. |
| [first](first.md) | [jvm]<br>abstract fun [first](first.md)(): [E](index.md)?<br>Return the first element in the list or null if the list is empty |
| [forEach](../-observable-collection/index.md#1532301601%2FFunctions%2F-519281799) | [jvm]<br>open fun [forEach](../-observable-collection/index.md#1532301601%2FFunctions%2F-519281799)(p0: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;in [E](index.md)&gt;) |
| [get](get.md) | [jvm]<br>abstract operator fun [get](get.md)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [E](index.md)?<br>Returns the element at the specified index in the list. |
| [indexOf](index-of.md) | [jvm]<br>abstract fun [indexOf](index-of.md)(element: [E](index.md)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns the index of the first occurrence of the e element in the list, or -1 if the specified element is not contained in the list. |
| [isEmpty](is-empty.md) | [jvm]<br>abstract fun [isEmpty](is-empty.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Returns true if the list contains no elements, false otherwise. |
| [iterator](index.md#-1651023311%2FFunctions%2F-519281799) | [jvm]<br>abstract operator override fun [iterator](index.md#-1651023311%2FFunctions%2F-519281799)(): [MutableIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterator/index.html)&lt;[E](index.md)&gt; |
| [last](last.md) | [jvm]<br>abstract fun [last](last.md)(): [E](index.md)?<br>Return the last element in the list or null if the list is empty |
| [missingTime](missing-time.md) | [jvm]<br>abstract fun [missingTime](missing-time.md)(element: [E](index.md)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?<br>Returns the time missing to expire or null if the list don't contain the element |
| [remove](remove.md) | [jvm]<br>abstract fun [remove](remove.md)(element: [E](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Removes an element from the list |
| [removeAt](remove-at.md) | [jvm]<br>abstract fun [removeAt](remove-at.md)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [E](index.md)?<br>Removes an element at the specified [index](remove-at.md) from the list. |
| [removeFirst](remove-first.md) | [jvm]<br>abstract fun [removeFirst](remove-first.md)(): [E](index.md)?<br>Removes the first element from the list. |
| [removeLast](remove-last.md) | [jvm]<br>abstract fun [removeLast](remove-last.md)(): [E](index.md)?<br>Removes the last element from the list. |
| [spliterator](../-expiration-list-impl/index.md#-1387152138%2FFunctions%2F-519281799) | [jvm]<br>open fun [spliterator](../-expiration-list-impl/index.md#-1387152138%2FFunctions%2F-519281799)(): [Spliterator](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)&lt;[E](index.md)&gt; |

## Properties

| Name | Summary |
|---|---|
| [size](size.md) | [jvm]<br>abstract val [size](size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns the count of elements in this list. |

## Inheritors

| Name |
|---|
| [ExpirationListImpl](../-expiration-list-impl/index.md) |
