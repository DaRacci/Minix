//[RacciCore](../../../index.md)/[me.racci.raccicore.api.utils.items.builders](../index.md)/[FireworkBuilder](index.md)/[power](power.md)

# power

[jvm]\
fun [power](power.md)(power: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [FireworkBuilder](index.md)

Sets the approximate power of the firework. Each level of power is half a second of flight time.

#### Return

[FireworkBuilder](index.md)

#### Since

3.0.1

## Parameters

jvm

| | |
|---|---|
| power | the power of the firework, from 0-128 |

## Throws

| | |
|---|---|
| [kotlin.IllegalArgumentException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/index.html) | if height<0 or height>128 |
