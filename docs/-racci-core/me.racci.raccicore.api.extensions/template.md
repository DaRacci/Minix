---
title: template
---
//[RacciCore](../../index.html)/[me.racci.raccicore.api.extensions](index.html)/[template](template.html)



# template



[jvm]\
inline fun MiniMessage.[template](template.html)(input: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), vararg template: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), *&gt;, builder: Component.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {}): Component



Parses a string into a component.



#### Return



the output component



#### Since



0.3.1



## Parameters


jvm

| | |
|---|---|
| input | The input string |
| template | Template pairs to resolve |
| builder | Block to apply to the component |




