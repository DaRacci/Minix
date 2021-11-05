//[RacciCore](../../../index.md)/[me.racci.raccicore](../index.md)/[RacciPlugin](index.md)

# RacciPlugin

[jvm]\
abstract class [RacciPlugin](index.md)(colour: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), prefix: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), spigotId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), bStatsId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : SuspendingJavaPlugin

Create the new plugin. All values of the constructor are nullable

## Parameters

jvm

| | |
|---|---|
| colour | The colour for console messages |
| prefix | The prefix for console messages |
| spigotId | The spigot ID for the plugin |
| bStatsId | The bStats ID for the plugin |

## Constructors

| | |
|---|---|
| [RacciPlugin](-racci-plugin.md) | [jvm]<br>fun [RacciPlugin](-racci-plugin.md)(colour: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = "", prefix: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = "", spigotId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, bStatsId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0) |

## Functions

| Name | Summary |
|---|---|
| [equals](index.md#-956268231%2FFunctions%2F-519281799) | [jvm]<br>operator override fun [equals](index.md#-956268231%2FFunctions%2F-519281799)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getClassLoader](index.md#-1264005818%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>fun [getClassLoader](index.md#-1264005818%2FFunctions%2F-519281799)(): @NotNull[ClassLoader](https://docs.oracle.com/javase/8/docs/api/java/lang/ClassLoader.html) |
| [getCommand](index.md#-1722902754%2FFunctions%2F-519281799) | [jvm]<br>@Nullable<br>open fun [getCommand](index.md#-1722902754%2FFunctions%2F-519281799)(@NotNullp0: @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): @NullablePluginCommand? |
| [getConfig](index.md#969075559%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>open override fun [getConfig](index.md#969075559%2FFunctions%2F-519281799)(): @NotNullFileConfiguration |
| [getDataFolder](index.md#-1341384527%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>override fun [getDataFolder](index.md#-1341384527%2FFunctions%2F-519281799)(): @NotNull[File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html) |
| [getDefaultBiomeProvider](index.md#-1131500269%2FFunctions%2F-519281799) | [jvm]<br>@Nullable<br>open override fun [getDefaultBiomeProvider](index.md#-1131500269%2FFunctions%2F-519281799)(@NotNullp0: @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), @Nullablep1: @Nullable[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?): @NullableBiomeProvider? |
| [getDefaultWorldGenerator](index.md#1284217961%2FFunctions%2F-519281799) | [jvm]<br>@Nullable<br>open override fun [getDefaultWorldGenerator](index.md#1284217961%2FFunctions%2F-519281799)(@NotNullp0: @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), @Nullablep1: @Nullable[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?): @NullableChunkGenerator? |
| [getDescription](index.md#-1721867755%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>override fun [getDescription](index.md#-1721867755%2FFunctions%2F-519281799)(): @NotNullPluginDescriptionFile |
| [getFile](index.md#1854065005%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>open fun [getFile](index.md#1854065005%2FFunctions%2F-519281799)(): @NotNull[File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html) |
| [getLog4JLogger](index.md#-360014269%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>open fun [getLog4JLogger](index.md#-360014269%2FFunctions%2F-519281799)(): @NotNullLogger |
| [getLogger](index.md#-190444327%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>open override fun [getLogger](index.md#-190444327%2FFunctions%2F-519281799)(): @NotNull[Logger](https://docs.oracle.com/javase/8/docs/api/java/util/logging/Logger.html) |
| [getName](index.md#-1617404175%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>override fun [getName](index.md#-1617404175%2FFunctions%2F-519281799)(): @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getPluginLoader](index.md#1789185283%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>override fun [getPluginLoader](index.md#1789185283%2FFunctions%2F-519281799)(): @NotNullPluginLoader |
| [getResource](index.md#1780025289%2FFunctions%2F-519281799) | [jvm]<br>@Nullable<br>open override fun [getResource](index.md#1780025289%2FFunctions%2F-519281799)(@NotNullp0: @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): @Nullable[InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html)? |
| [getServer](index.md#1660097158%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>override fun [getServer](index.md#1660097158%2FFunctions%2F-519281799)(): @NotNullServer |
| [getSLF4JLogger](index.md#663398778%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>open fun [getSLF4JLogger](index.md#663398778%2FFunctions%2F-519281799)(): @NotNullLogger |
| [getTextResource](index.md#1065257046%2FFunctions%2F-519281799) | [jvm]<br>@Nullable<br>fun [getTextResource](index.md#1065257046%2FFunctions%2F-519281799)(@NotNullp0: @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): @Nullable[Reader](https://docs.oracle.com/javase/8/docs/api/java/io/Reader.html)? |
| [hashCode](index.md#-1047022707%2FFunctions%2F-519281799) | [jvm]<br>override fun [hashCode](index.md#-1047022707%2FFunctions%2F-519281799)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [init](index.md#263625417%2FFunctions%2F-519281799) | [jvm]<br>fun [init](index.md#263625417%2FFunctions%2F-519281799)(@NotNullp0: @NotNullPluginLoader, @NotNullp1: @NotNullServer, @NotNullp2: @NotNullPluginDescriptionFile, @NotNullp3: @NotNull[File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html), @NotNullp4: @NotNull[File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html), @NotNullp5: @NotNull[ClassLoader](https://docs.oracle.com/javase/8/docs/api/java/lang/ClassLoader.html)) |
| [isEnabled](index.md#-655197240%2FFunctions%2F-519281799) | [jvm]<br>override fun [isEnabled](index.md#-655197240%2FFunctions%2F-519281799)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isNaggable](index.md#-1116561404%2FFunctions%2F-519281799) | [jvm]<br>override fun [isNaggable](index.md#-1116561404%2FFunctions%2F-519281799)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [onCommand](index.md#1148868763%2FFunctions%2F-519281799) | [jvm]<br>open override fun [onCommand](index.md#1148868763%2FFunctions%2F-519281799)(@NotNullp0: @NotNullCommandSender, @NotNullp1: @NotNullCommand, @NotNullp2: @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), @NotNullp3: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;@NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [onDisable](index.md#67691972%2FFunctions%2F-519281799) | [jvm]<br>open override fun [onDisable](index.md#67691972%2FFunctions%2F-519281799)() |
| [onDisableAsync](on-disable-async.md) | [jvm]<br>open suspend override fun [onDisableAsync](on-disable-async.md)() |
| [onEnable](index.md#1336450507%2FFunctions%2F-519281799) | [jvm]<br>open override fun [onEnable](index.md#1336450507%2FFunctions%2F-519281799)() |
| [onEnableAsync](on-enable-async.md) | [jvm]<br>open suspend override fun [onEnableAsync](on-enable-async.md)() |
| [onLoad](index.md#-1261380600%2FFunctions%2F-519281799) | [jvm]<br>open override fun [onLoad](index.md#-1261380600%2FFunctions%2F-519281799)() |
| [onLoadAsync](on-load-async.md) | [jvm]<br>open suspend override fun [onLoadAsync](on-load-async.md)() |
| [onTabComplete](index.md#193072766%2FFunctions%2F-519281799) | [jvm]<br>@Nullable<br>open override fun [onTabComplete](index.md#193072766%2FFunctions%2F-519281799)(@NotNullp0: @NotNullCommandSender, @NotNullp1: @NotNullCommand, @NotNullp2: @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), @NotNullp3: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;@NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;): @Nullable[MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;? |
| [reload](reload.md) | [jvm]<br>suspend fun [reload](reload.md)() |
| [reloadConfig](index.md#-1959235648%2FFunctions%2F-519281799) | [jvm]<br>open override fun [reloadConfig](index.md#-1959235648%2FFunctions%2F-519281799)() |
| [saveConfig](index.md#-1998581028%2FFunctions%2F-519281799) | [jvm]<br>open override fun [saveConfig](index.md#-1998581028%2FFunctions%2F-519281799)() |
| [saveDefaultConfig](index.md#-1312772871%2FFunctions%2F-519281799) | [jvm]<br>open override fun [saveDefaultConfig](index.md#-1312772871%2FFunctions%2F-519281799)() |
| [saveResource](index.md#-1107407536%2FFunctions%2F-519281799) | [jvm]<br>open override fun [saveResource](index.md#-1107407536%2FFunctions%2F-519281799)(@NotNullp0: @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), p1: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |
| [setEnabled](index.md#496945089%2FFunctions%2F-519281799) | [jvm]<br>fun [setEnabled](index.md#496945089%2FFunctions%2F-519281799)(p0: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |
| [setNaggable](index.md#67101717%2FFunctions%2F-519281799) | [jvm]<br>override fun [setNaggable](index.md#67101717%2FFunctions%2F-519281799)(p0: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |
| [toString](index.md#1897086895%2FFunctions%2F-519281799) | [jvm]<br>@NotNull<br>open override fun [toString](index.md#1897086895%2FFunctions%2F-519281799)(): @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

## Properties

| Name | Summary |
|---|---|
| [bStatsId](b-stats-id.md) | [jvm]<br>val [bStatsId](b-stats-id.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |
| [colour](colour.md) | [jvm]<br>val [colour](colour.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [commandManager](command-manager.md) | [jvm]<br>lateinit var [commandManager](command-manager.md): PaperCommandManager |
| [log](log.md) | [jvm]<br>val [log](log.md): [Log](../-log/index.md) |
| [logger](index.md#625757323%2FProperties%2F-519281799) | [jvm]<br>val [logger](index.md#625757323%2FProperties%2F-519281799): [Logger](https://docs.oracle.com/javase/8/docs/api/java/util/logging/Logger.html) |
| [prefix](prefix.md) | [jvm]<br>val [prefix](prefix.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [spigotId](spigot-id.md) | [jvm]<br>val [spigotId](spigot-id.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |

## Inheritors

| Name |
|---|
| [RacciCore](../-racci-core/index.md) |

## Extensions

| Name | Summary |
|---|---|
| [dispatcher](../../me.racci.raccicore.skedule/dispatcher.md) | [jvm]<br>@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")<br>~~fun~~ [RacciPlugin](index.md)~~.~~[~~dispatcher~~](../../me.racci.raccicore.skedule/dispatcher.md)~~(~~~~async~~~~:~~ [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) ~~= false~~~~)~~~~:~~ [BukkitDispatcher](../../me.racci.raccicore.skedule/-bukkit-dispatcher/index.md) |
| [listen](../../me.racci.raccicore.utils/listen.md) | [jvm]<br>inline fun &lt;[T](../../me.racci.raccicore.utils/listen.md) : Event&gt; [RacciPlugin](index.md).[listen](../../me.racci.raccicore.utils/listen.md)(priority: EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, crossinline callback: ([T](../../me.racci.raccicore.utils/listen.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [runnable](../../me.racci.raccicore.runnables/runnable.md) | [jvm]<br>suspend fun [RacciPlugin](index.md).[runnable](../../me.racci.raccicore.runnables/runnable.md)(async: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, repeating: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, delay: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 0L, period: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 0L, unit: [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [schedule](../../me.racci.raccicore.skedule/schedule.md) | [jvm]<br>@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")<br>~~fun~~ [RacciPlugin](index.md)~~.~~[~~schedule~~](../../me.racci.raccicore.skedule/schedule.md)~~(~~~~initialContext~~~~:~~ [SynchronizationContext](../../me.racci.raccicore.skedule/-synchronization-context/index.md) ~~= SynchronizationContext.SYNC~~~~,~~ ~~co~~~~:~~ suspend [BukkitSchedulerController](../../me.racci.raccicore.skedule/-bukkit-scheduler-controller/index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)~~)~~~~:~~ [CoroutineTask](../../me.racci.raccicore.skedule/-coroutine-task/index.md) |
