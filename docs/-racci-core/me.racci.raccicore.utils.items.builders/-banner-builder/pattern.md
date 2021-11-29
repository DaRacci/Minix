//[RacciCore](../../../index.md)/[me.racci.raccicore.api.utils.items.builders](../index.md)/[BannerBuilder](index.md)/[pattern](pattern.md)

# pattern

[jvm]\
fun [pattern](pattern.md)(color: DyeColor, pattern: PatternType): [BannerBuilder](index.md)

Adds a new pattern on top of the existing patterns

#### Return

[BannerBuilder](index.md)

#### Since

3.0.1

## Parameters

jvm

| | |
|---|---|
| color | the pattern color |
| pattern | the pattern type |

[jvm]\
fun [pattern](pattern.md)(vararg pattern: Pattern?): [BannerBuilder](index.md)

Adds new patterns on top of the existing patterns

#### Return

[BannerBuilder](index.md)

#### Since

3.0.1

## Parameters

jvm

| | |
|---|---|
| pattern | the patterns |

[jvm]\
fun [pattern](pattern.md)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), color: DyeColor, pattern: PatternType): [BannerBuilder](index.md)

Sets the pattern at the specified index

#### Return

[BannerBuilder](index.md)

#### Since

3.0.1

## Parameters

jvm

| | |
|---|---|
| index | the index |
| color | the pattern color |
| pattern | the pattern type |

## Throws

| | |
|---|---|
| [kotlin.IndexOutOfBoundsException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-index-out-of-bounds-exception/index.html) | when index is not in [0, BannerMeta.numberOfPatterns) range |
