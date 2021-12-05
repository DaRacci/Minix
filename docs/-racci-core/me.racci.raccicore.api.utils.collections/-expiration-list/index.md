---
title: ExpirationList
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.collections](../index.html)/[ExpirationList](index.html)



# ExpirationList



[jvm]\
interface [ExpirationList](index.html)&lt;[E](index.html)&gt; : [MutableIterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterable/index.html)&lt;[E](index.html)&gt;



## Functions


| Name | Summary |
|---|---|
| [add](add.html) | [jvm]<br>abstract fun [add](add.html)(element: [E](index.html), expireTime: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), onExpire: [OnExpireCallback](../index.html#-1395177404%2FClasslikes%2F863300109)&lt;[E](index.html)&gt;? = null)<br>Add the element to the list with an expiration time. |
| [addFirst](add-first.html) | [jvm]<br>abstract fun [addFirst](add-first.html)(element: [E](index.html), expireTime: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), onExpire: [OnExpireCallback](../index.html#-1395177404%2FClasslikes%2F863300109)&lt;[E](index.html)&gt;? = null)<br>Add the element in the start of list with an expiration time. |
| [clear](clear.html) | [jvm]<br>abstract fun [clear](clear.html)()<br>Removes all elements from this list. |
| [contains](contains.html) | [jvm]<br>abstract operator fun [contains](contains.html)(element: [E](index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if the specified element is contained in this list. |
| [first](first.html) | [jvm]<br>abstract fun [first](first.html)(): [E](index.html)?<br>Return the first element in the list or null if the list is empty |
| [forEach](../../me.racci.raccicore.api.utils.minecraft/-pos-range/index.html#1532301601%2FFunctions%2F863300109) | [jvm]<br>open fun [forEach](../../me.racci.raccicore.api.utils.minecraft/-pos-range/index.html#1532301601%2FFunctions%2F863300109)(p0: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;in [E](index.html)&gt;) |
| [get](get.html) | [jvm]<br>abstract operator fun [get](get.html)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [E](index.html)?<br>Returns the element at the specified index in the list. |
| [indexOf](index-of.html) | [jvm]<br>abstract fun [indexOf](index-of.html)(element: [E](index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns the index of the first occurrence of the e element in the list, or -1 if the specified element is not contained in the list. |
| [isEmpty](is-empty.html) | [jvm]<br>abstract fun [isEmpty](is-empty.html)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Returns true if the list contains no elements, false otherwise. |
| [iterator](index.html#-1651023311%2FFunctions%2F863300109) | [jvm]<br>abstract operator override fun [iterator](index.html#-1651023311%2FFunctions%2F863300109)(): [MutableIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterator/index.html)&lt;[E](index.html)&gt; |
| [last](last.html) | [jvm]<br>abstract fun [last](last.html)(): [E](index.html)?<br>Return the last element in the list or null if the list is empty |
| [missingTime](missing-time.html) | [jvm]<br>abstract fun [missingTime](missing-time.html)(element: [E](index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?<br>Returns the time missing expiring or null if the list don't contain the element |
| [remove](remove.html) | [jvm]<br>abstract fun [remove](remove.html)(element: [E](index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Removes an element from the list |
| [removeAt](remove-at.html) | [jvm]<br>abstract fun [removeAt](remove-at.html)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [E](index.html)?<br>Removes an element at the specified [index](remove-at.html) from the list. |
| [removeFirst](remove-first.html) | [jvm]<br>abstract fun [removeFirst](remove-first.html)(): [E](index.html)?<br>Removes the first element from the list. |
| [removeLast](remove-last.html) | [jvm]<br>abstract fun [removeLast](remove-last.html)(): [E](index.html)?<br>Removes the last element from the list. |
| [spliterator](../../me.racci.raccicore.api.utils.minecraft/-pos-range/index.html#-1387152138%2FFunctions%2F863300109) | [jvm]<br>open fun [spliterator](../../me.racci.raccicore.api.utils.minecraft/-pos-range/index.html#-1387152138%2FFunctions%2F863300109)(): [Spliterator](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)&lt;[E](index.html)&gt; |


## Properties


| Name | Summary |
|---|---|
| [size](size.html) | [jvm]<br>abstract val [size](size.html): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns the count of elements in this list. |


## Inheritors


| Name |
|---|
| [ExpirationListImpl](../-expiration-list-impl/index.html) |

