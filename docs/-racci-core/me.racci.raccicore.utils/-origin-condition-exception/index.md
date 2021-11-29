//[RacciCore](../../../index.md)/[me.racci.raccicore.api.utils](../index.md)/[OriginConditionException](index.md)

# OriginConditionException

[jvm]\
class [OriginConditionException](index.md)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [Exception](https://docs.oracle.com/javase/8/docs/api/java/lang/Exception.html)

Origin condition exception

## Parameters

jvm

| | |
|---|---|
| message |  |

## Constructors

| | |
|---|---|
| [OriginConditionException](-origin-condition-exception.md) | [jvm]<br>fun [OriginConditionException](-origin-condition-exception.md)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |

## Functions

| Name | Summary |
|---|---|
| [addSuppressed](index.md#282858770%2FFunctions%2F-519281799) | [jvm]<br>fun [addSuppressed](index.md#282858770%2FFunctions%2F-519281799)(p0: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) |
| [fillInStackTrace](index.md#-1102069925%2FFunctions%2F-519281799) | [jvm]<br>open fun [fillInStackTrace](index.md#-1102069925%2FFunctions%2F-519281799)(): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html) |
| [getLocalizedMessage](index.md#1043865560%2FFunctions%2F-519281799) | [jvm]<br>open fun [getLocalizedMessage](index.md#1043865560%2FFunctions%2F-519281799)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getStackTrace](index.md#2050903719%2FFunctions%2F-519281799) | [jvm]<br>open fun [getStackTrace](index.md#2050903719%2FFunctions%2F-519281799)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[StackTraceElement](https://docs.oracle.com/javase/8/docs/api/java/lang/StackTraceElement.html)&gt; |
| [getSuppressed](index.md#672492560%2FFunctions%2F-519281799) | [jvm]<br>fun [getSuppressed](index.md#672492560%2FFunctions%2F-519281799)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)&gt; |
| [initCause](index.md#-418225042%2FFunctions%2F-519281799) | [jvm]<br>open fun [initCause](index.md#-418225042%2FFunctions%2F-519281799)(p0: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html) |
| [printStackTrace](index.md#-1769529168%2FFunctions%2F-519281799) | [jvm]<br>open fun [printStackTrace](index.md#-1769529168%2FFunctions%2F-519281799)()<br>open fun [printStackTrace](index.md#1841853697%2FFunctions%2F-519281799)(p0: [PrintStream](https://docs.oracle.com/javase/8/docs/api/java/io/PrintStream.html))<br>open fun [printStackTrace](index.md#1175535278%2FFunctions%2F-519281799)(p0: [PrintWriter](https://docs.oracle.com/javase/8/docs/api/java/io/PrintWriter.html)) |
| [setStackTrace](index.md#2135801318%2FFunctions%2F-519281799) | [jvm]<br>open fun [setStackTrace](index.md#2135801318%2FFunctions%2F-519281799)(p0: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[StackTraceElement](https://docs.oracle.com/javase/8/docs/api/java/lang/StackTraceElement.html)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [cause](index.md#-654012527%2FProperties%2F-519281799) | [jvm]<br>open val [cause](index.md#-654012527%2FProperties%2F-519281799): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)? |
| [message](index.md#1824300659%2FProperties%2F-519281799) | [jvm]<br>open val [message](index.md#1824300659%2FProperties%2F-519281799): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
