//[RacciCore](../../index.md)/[me.racci.raccicore.api.utils.strings](index.md)

# Package me.racci.raccicore.api.utils.strings

## Types

| Name | Summary |
|---|---|
| [Legacy](-legacy/index.md) | [jvm]<br>class [Legacy](-legacy/index.md)<br>Legacy |
| [LegacyUtils](-legacy-utils/index.md) | [jvm]<br>object [LegacyUtils](-legacy-utils/index.md) |

## Functions

| Name | Summary |
|---|---|
| [colour](colour.md) | [jvm]<br>fun [colour](colour.md)(string: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, parseHex: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>fun [colour](colour.md)(string: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, parseHex: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>[jvm]<br>fun [colour](colour.md)(list: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, parseHex: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;<br>Colour |
| [colouredTextOf](coloured-text-of.md) | [jvm]<br>fun [colouredTextOf](coloured-text-of.md)(string: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), builder: TextComponent.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {}): @NotNullTextComponent<br>fun [colouredTextOf](coloured-text-of.md)(list: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, builder: TextComponent.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {}): [ArrayList](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html)&lt;Component&gt; |
| [colouredTextOf1](coloured-text-of1.md) | [jvm]<br>fun [colouredTextOf1](coloured-text-of1.md)(string: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), builder: TextComponent.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {}): TextComponent |
| [gradient](gradient.md) | [jvm]<br>fun [gradient](gradient.md)(h1: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html), s1: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html), v1: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html), h2: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html), s2: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html), v2: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html), string: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): Component |
| [replace](replace.md) | [jvm]<br>fun [replace](replace.md)(source: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), vararg replace: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [textOf](text-of.md) | [jvm]<br>fun [textOf](text-of.md)(string: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), builder: TextComponent.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {}): TextComponent |
