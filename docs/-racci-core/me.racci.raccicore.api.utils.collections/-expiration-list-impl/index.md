---
title: ExpirationListImpl
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.collections](../index.html)/[ExpirationListImpl](index.html)



# ExpirationListImpl



[jvm]\
class [ExpirationListImpl](index.html)&lt;[E](index.html)&gt;(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html)) : [ExpirationList](../-expiration-list/index.html)&lt;[E](index.html)&gt;



## Functions


| Name | Summary |
|---|---|
| [add](add.html) | [jvm]<br>open override fun [add](add.html)(element: [E](index.html), expireTime: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), onExpire: [OnExpireCallback](../index.html#-1395177404%2FClasslikes%2F863300109)&lt;[E](index.html)&gt;?)<br>Add the element to the list with an expiration time. |
| [addFirst](add-first.html) | [jvm]<br>open override fun [addFirst](add-first.html)(element: [E](index.html), expireTime: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), onExpire: [OnExpireCallback](../index.html#-1395177404%2FClasslikes%2F863300109)&lt;[E](index.html)&gt;?)<br>Add the element in the start of list with an expiration time. |
| [clear](clear.html) | [jvm]<br>open override fun [clear](clear.html)()<br>Removes all elements from this list. |
| [contains](contains.html) | [jvm]<br>open operator override fun [contains](contains.html)(element: [E](index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if the specified element is contained in this list. |
| [first](first.html) | [jvm]<br>open override fun [first](first.html)(): [E](index.html)?<br>Return the first element in the list or null if the list is empty |
| [forEach](../../me.racci.raccicore.api.utils.minecraft/-pos-range/index.html#1532301601%2FFunctions%2F863300109) | [jvm]<br>open fun [forEach](../../me.racci.raccicore.api.utils.minecraft/-pos-range/index.html#1532301601%2FFunctions%2F863300109)(p0: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;in [E](index.html)&gt;) |
| [get](get.html) | [jvm]<br>open operator override fun [get](get.html)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [E](index.html)?<br>Returns the element at the specified index in the list. |
| [indexOf](index-of.html) | [jvm]<br>open override fun [indexOf](index-of.html)(element: [E](index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns the index of the first occurrence of the e element in the list, or -1 if the specified element is not contained in the list. |
| [isEmpty](is-empty.html) | [jvm]<br>open override fun [isEmpty](is-empty.html)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Returns true if the list contains no elements, false otherwise. |
| [iterator](iterator.html) | [jvm]<br>open operator override fun [iterator](iterator.html)(): [MutableIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterator/index.html)&lt;[E](index.html)&gt; |
| [last](last.html) | [jvm]<br>open override fun [last](last.html)(): [E](index.html)?<br>Return the last element in the list or null if the list is empty |
| [missingTime](missing-time.html) | [jvm]<br>open override fun [missingTime](missing-time.html)(element: [E](index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?<br>Returns the time missing expiring or null if the list don't contain the element |
| [remove](remove.html) | [jvm]<br>open override fun [remove](remove.html)(element: [E](index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Removes an element from the list |
| [removeAt](remove-at.html) | [jvm]<br>open override fun [removeAt](remove-at.html)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [E](index.html)?<br>Removes an element at the specified [index](remove-at.html) from the list. |
| [removeFirst](remove-first.html) | [jvm]<br>open override fun [removeFirst](remove-first.html)(): [E](index.html)?<br>Removes the first element from the list. |
| [removeLast](remove-last.html) | [jvm]<br>open override fun [removeLast](remove-last.html)(): [E](index.html)?<br>Removes the last element from the list. |
| [spliterator](../../me.racci.raccicore.api.utils.minecraft/-pos-range/index.html#-1387152138%2FFunctions%2F863300109) | [jvm]<br>open fun [spliterator](../../me.racci.raccicore.api.utils.minecraft/-pos-range/index.html#-1387152138%2FFunctions%2F863300109)(): [Spliterator](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)&lt;[E](index.html)&gt; |


## Properties


| Name | Summary |
|---|---|
| [_size](_size.html) | [jvm]<br>var [_size](_size.html): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |
| [size](size.html) | [jvm]<br>open override val [size](size.html): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns the count of elements in this list. |

