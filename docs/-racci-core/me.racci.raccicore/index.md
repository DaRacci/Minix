//[RacciCore](../../index.md)/[me.racci.raccicore](index.md)

# Package me.racci.raccicore

## Types

| Name | Summary |
|---|---|
| [Level](-level/index.md) | [jvm]<br>enum [Level](-level/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[Level](-level/index.md)&gt; <br>Level |
| [Log](-log/index.md) | [jvm]<br>class [Log](-log/index.md)(prefix: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), debugMode: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |
| [RacciCore](-racci-core/index.md) | [jvm]<br>class [RacciCore](-racci-core/index.md) : [RacciPlugin](-racci-plugin/index.md) |
| [RacciPlugin](-racci-plugin/index.md) | [jvm]<br>abstract class [RacciPlugin](-racci-plugin/index.md)(colour: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), prefix: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), spigotId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), bStatsId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : SuspendingJavaPlugin<br>Create the new plugin. All values of the constructor are nullable |

## Functions

| Name | Summary |
|---|---|
| [debug](debug.md) | [jvm]<br>@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")<br>~~fun~~ [~~debug~~](debug.md)~~(~~~~message~~~~:~~ [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)~~)~~ |
| [error](error.md) | [jvm]<br>@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")<br>~~fun~~ [~~error~~](error.md)~~(~~~~message~~~~:~~ [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)~~)~~ |
| [info](info.md) | [jvm]<br>@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")<br>~~fun~~ [~~info~~](info.md)~~(~~~~message~~~~:~~ [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)~~)~~ |
| [log](log.md) | [jvm]<br>@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")<br>~~fun~~ [~~log~~](log.md)~~(~~~~level~~~~:~~ [Level](-level/index.md)~~,~~ ~~message~~~~:~~ [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)~~)~~ |
| [outline](outline.md) | [jvm]<br>@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")<br>~~fun~~ [~~outline~~](outline.md)~~(~~~~message~~~~:~~ [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)~~)~~ |
| [success](success.md) | [jvm]<br>@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")<br>~~fun~~ [~~success~~](success.md)~~(~~~~message~~~~:~~ [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)~~)~~ |
| [warning](warning.md) | [jvm]<br>@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")<br>~~fun~~ [~~warning~~](warning.md)~~(~~~~message~~~~:~~ [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)~~)~~ |
