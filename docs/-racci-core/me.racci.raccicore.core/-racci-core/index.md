---
title: RacciCore
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.core](../index.html)/[RacciCore](index.html)



# RacciCore



[jvm]\
class [RacciCore](index.html) : [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html)



## Functions


| Name | Summary |
|---|---|
| [equals](index.html#-956268231%2FFunctions%2F863300109) | [jvm]<br>operator override fun [equals](index.html#-956268231%2FFunctions%2F863300109)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getClassLoader](index.html#-1264005818%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>fun [getClassLoader](index.html#-1264005818%2FFunctions%2F863300109)(): @NotNull[ClassLoader](https://docs.oracle.com/javase/8/docs/api/java/lang/ClassLoader.html) |
| [getCommand](index.html#-1722902754%2FFunctions%2F863300109) | [jvm]<br>@Nullable<br>open fun [getCommand](index.html#-1722902754%2FFunctions%2F863300109)(@NotNullp0: @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): @NullablePluginCommand? |
| [getConfig](index.html#969075559%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>open override fun [getConfig](index.html#969075559%2FFunctions%2F863300109)(): @NotNullFileConfiguration |
| [getDataFolder](index.html#-1341384527%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>override fun [getDataFolder](index.html#-1341384527%2FFunctions%2F863300109)(): @NotNull[File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html) |
| [getDefaultBiomeProvider](index.html#-1131500269%2FFunctions%2F863300109) | [jvm]<br>@Nullable<br>open override fun [getDefaultBiomeProvider](index.html#-1131500269%2FFunctions%2F863300109)(@NotNullp0: @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), @Nullablep1: @Nullable[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?): @NullableBiomeProvider? |
| [getDefaultWorldGenerator](index.html#1284217961%2FFunctions%2F863300109) | [jvm]<br>@Nullable<br>open override fun [getDefaultWorldGenerator](index.html#1284217961%2FFunctions%2F863300109)(@NotNullp0: @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), @Nullablep1: @Nullable[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?): @NullableChunkGenerator? |
| [getDescription](index.html#-1721867755%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>override fun [getDescription](index.html#-1721867755%2FFunctions%2F863300109)(): @NotNullPluginDescriptionFile |
| [getFile](index.html#1854065005%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>open fun [getFile](index.html#1854065005%2FFunctions%2F863300109)(): @NotNull[File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html) |
| [getLog4JLogger](index.html#-360014269%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>open fun [getLog4JLogger](index.html#-360014269%2FFunctions%2F863300109)(): @NotNullLogger |
| [getLogger](index.html#-190444327%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>open override fun [getLogger](index.html#-190444327%2FFunctions%2F863300109)(): @NotNull[Logger](https://docs.oracle.com/javase/8/docs/api/java/util/logging/Logger.html) |
| [getName](index.html#-1617404175%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>override fun [getName](index.html#-1617404175%2FFunctions%2F863300109)(): @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getPluginLoader](index.html#1789185283%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>override fun [getPluginLoader](index.html#1789185283%2FFunctions%2F863300109)(): @NotNullPluginLoader |
| [getResource](index.html#1780025289%2FFunctions%2F863300109) | [jvm]<br>@Nullable<br>open override fun [getResource](index.html#1780025289%2FFunctions%2F863300109)(@NotNullp0: @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): @Nullable[InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html)? |
| [getServer](index.html#1660097158%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>override fun [getServer](index.html#1660097158%2FFunctions%2F863300109)(): @NotNullServer |
| [getSLF4JLogger](index.html#663398778%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>open fun [getSLF4JLogger](index.html#663398778%2FFunctions%2F863300109)(): @NotNullLogger |
| [getTextResource](index.html#1065257046%2FFunctions%2F863300109) | [jvm]<br>@Nullable<br>fun [getTextResource](index.html#1065257046%2FFunctions%2F863300109)(@NotNullp0: @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): @Nullable[Reader](https://docs.oracle.com/javase/8/docs/api/java/io/Reader.html)? |
| [handleAfterLoad](handle-after-load.html) | [jvm]<br>open suspend override fun [handleAfterLoad](handle-after-load.html)()<br>This will be called after your Plugin is done loading and RacciCore has finished its loading process for your Plugin. |
| [handleDisable](../../me.racci.raccicore.api.plugin/-racci-plugin/handle-disable.html) | [jvm]<br>open suspend fun [handleDisable](../../me.racci.raccicore.api.plugin/-racci-plugin/handle-disable.html)()<br>This is triggered when your Plugin is being disabled by the Server, Please use this to clean up your Plugin to not leak resources. |
| [handleEnable](handle-enable.html) | [jvm]<br>open suspend override fun [handleEnable](handle-enable.html)()<br>This is called Once the Plugin is ready to accept and register events, commands etc. |
| [handleLoad](../../me.racci.raccicore.api.plugin/-racci-plugin/handle-load.html) | [jvm]<br>open suspend fun [handleLoad](../../me.racci.raccicore.api.plugin/-racci-plugin/handle-load.html)()<br>This is called when the server picks up your Plugin and has begun loading it. |
| [handleReload](../../me.racci.raccicore.api.plugin/-racci-plugin/handle-reload.html) | [jvm]<br>open suspend fun [handleReload](../../me.racci.raccicore.api.plugin/-racci-plugin/handle-reload.html)()<br>This will be called whenever [RacciPlugin.reload](../../me.racci.raccicore.api.plugin/-racci-plugin/reload.html) is called. |
| [hashCode](index.html#-1047022707%2FFunctions%2F863300109) | [jvm]<br>override fun [hashCode](index.html#-1047022707%2FFunctions%2F863300109)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [init](index.html#263625417%2FFunctions%2F863300109) | [jvm]<br>fun [init](index.html#263625417%2FFunctions%2F863300109)(@NotNullp0: @NotNullPluginLoader, @NotNullp1: @NotNullServer, @NotNullp2: @NotNullPluginDescriptionFile, @NotNullp3: @NotNull[File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html), @NotNullp4: @NotNull[File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html), @NotNullp5: @NotNull[ClassLoader](https://docs.oracle.com/javase/8/docs/api/java/lang/ClassLoader.html)) |
| [isEnabled](index.html#-655197240%2FFunctions%2F863300109) | [jvm]<br>override fun [isEnabled](index.html#-655197240%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isNaggable](index.html#-1116561404%2FFunctions%2F863300109) | [jvm]<br>override fun [isNaggable](index.html#-1116561404%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [onCommand](index.html#1148868763%2FFunctions%2F863300109) | [jvm]<br>open override fun [onCommand](index.html#1148868763%2FFunctions%2F863300109)(@NotNullp0: @NotNullCommandSender, @NotNullp1: @NotNullCommand, @NotNullp2: @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), @NotNullp3: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;@NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [onDisable](index.html#67691972%2FFunctions%2F863300109) | [jvm]<br>open override fun [onDisable](index.html#67691972%2FFunctions%2F863300109)() |
| [onDisableAsync](../../me.racci.raccicore.api.plugin/-racci-plugin/on-disable-async.html) | [jvm]<br>@ApiStatus.NonExtendable<br>open suspend override fun [onDisableAsync](../../me.racci.raccicore.api.plugin/-racci-plugin/on-disable-async.html)()<br>**DO NOT OVERRIDE** |
| [onEnable](index.html#1336450507%2FFunctions%2F863300109) | [jvm]<br>open override fun [onEnable](index.html#1336450507%2FFunctions%2F863300109)() |
| [onEnableAsync](../../me.racci.raccicore.api.plugin/-racci-plugin/on-enable-async.html) | [jvm]<br>@ApiStatus.NonExtendable<br>open suspend override fun [onEnableAsync](../../me.racci.raccicore.api.plugin/-racci-plugin/on-enable-async.html)()<br>**DO NOT OVERRIDE** |
| [onLoad](index.html#-1261380600%2FFunctions%2F863300109) | [jvm]<br>open override fun [onLoad](index.html#-1261380600%2FFunctions%2F863300109)() |
| [onLoadAsync](on-load-async.html) | [jvm]<br>open suspend override fun [onLoadAsync](on-load-async.html)()<br>**DO NOT OVERRIDE** |
| [onTabComplete](index.html#193072766%2FFunctions%2F863300109) | [jvm]<br>@Nullable<br>open override fun [onTabComplete](index.html#193072766%2FFunctions%2F863300109)(@NotNullp0: @NotNullCommandSender, @NotNullp1: @NotNullCommand, @NotNullp2: @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), @NotNullp3: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;@NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;): @Nullable[MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;? |
| [registerCommands](register-commands.html) | [jvm]<br>open suspend override fun [registerCommands](register-commands.html)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;BaseCommand&gt;<br>The returned list of BaseCommand's will be registered, and enabled during the enable process. |
| [registerLifecycles](register-lifecycles.html) | [jvm]<br>open suspend override fun [registerLifecycles](register-lifecycles.html)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[LifecycleListener](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/index.html)&lt;*&gt;, [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt;&gt;<br>The returned life of [LifecycleListener](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/index.html)'s will be registered, and enabled during the enable process. |
| [registerListeners](register-listeners.html) | [jvm]<br>open suspend override fun [registerListeners](register-listeners.html)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[KotlinListener](../../me.racci.raccicore.api.extensions/-kotlin-listener/index.html)&gt;<br>The returned list of [KotlinListener](../../me.racci.raccicore.api.extensions/-kotlin-listener/index.html)'s will be registered, and enabled during the enable process. |
| [reload](../../me.racci.raccicore.api.plugin/-racci-plugin/reload.html) | [jvm]<br>@ApiStatus.NonExtendable<br>suspend fun [reload](../../me.racci.raccicore.api.plugin/-racci-plugin/reload.html)() |
| [reloadConfig](index.html#-1959235648%2FFunctions%2F863300109) | [jvm]<br>open override fun [reloadConfig](index.html#-1959235648%2FFunctions%2F863300109)() |
| [saveConfig](index.html#-1998581028%2FFunctions%2F863300109) | [jvm]<br>open override fun [saveConfig](index.html#-1998581028%2FFunctions%2F863300109)() |
| [saveDefaultConfig](index.html#-1312772871%2FFunctions%2F863300109) | [jvm]<br>open override fun [saveDefaultConfig](index.html#-1312772871%2FFunctions%2F863300109)() |
| [saveResource](index.html#-1107407536%2FFunctions%2F863300109) | [jvm]<br>open override fun [saveResource](index.html#-1107407536%2FFunctions%2F863300109)(@NotNullp0: @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), p1: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |
| [setEnabled](index.html#496945089%2FFunctions%2F863300109) | [jvm]<br>fun [setEnabled](index.html#496945089%2FFunctions%2F863300109)(p0: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |
| [setNaggable](index.html#67101717%2FFunctions%2F863300109) | [jvm]<br>override fun [setNaggable](index.html#67101717%2FFunctions%2F863300109)(p0: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |
| [toString](index.html#1897086895%2FFunctions%2F863300109) | [jvm]<br>@NotNull<br>open override fun [toString](index.html#1897086895%2FFunctions%2F863300109)(): @NotNull[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |


## Properties


| Name | Summary |
|---|---|
| [bStatsId](../../me.racci.raccicore.api.plugin/-racci-plugin/b-stats-id.html) | [jvm]<br>val [bStatsId](../../me.racci.raccicore.api.plugin/-racci-plugin/b-stats-id.html): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0<br>The ID of your plugin on bStats. |
| [commandManager](../../me.racci.raccicore.api.plugin/-racci-plugin/command-manager.html) | [jvm]<br>var [commandManager](../../me.racci.raccicore.api.plugin/-racci-plugin/command-manager.html): PaperCommandManager |
| [lifecycleDisableOrder](../../me.racci.raccicore.api.plugin/-racci-plugin/lifecycle-disable-order.html) | [jvm]<br>val [lifecycleDisableOrder](../../me.racci.raccicore.api.plugin/-racci-plugin/lifecycle-disable-order.html): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Lifecycle](../../me.racci.raccicore.api.lifecycle/-lifecycle/index.html)&gt; |
| [lifecycleListeners](../../me.racci.raccicore.api.plugin/-racci-plugin/lifecycle-listeners.html) | [jvm]<br>val [lifecycleListeners](../../me.racci.raccicore.api.plugin/-racci-plugin/lifecycle-listeners.html): [HashSet](https://docs.oracle.com/javase/8/docs/api/java/util/HashSet.html)&lt;[Lifecycle](../../me.racci.raccicore.api.lifecycle/-lifecycle/index.html)&gt; |
| [log](../../me.racci.raccicore.api.plugin/-racci-plugin/log.html) | [jvm]<br>val [log](../../me.racci.raccicore.api.plugin/-racci-plugin/log.html): [Log](../../me.racci.raccicore.api.plugin/-log/index.html) |
| [logger](index.html#625757323%2FProperties%2F863300109) | [jvm]<br>val [logger](index.html#625757323%2FProperties%2F863300109): [Logger](https://docs.oracle.com/javase/8/docs/api/java/util/logging/Logger.html) |
| [prefix](../../me.racci.raccicore.api.plugin/-racci-plugin/prefix.html) | [jvm]<br>val [prefix](../../me.racci.raccicore.api.plugin/-racci-plugin/prefix.html): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The prefix for logging in console, this supports colours. |
| [spigotId](../../me.racci.raccicore.api.plugin/-racci-plugin/spigot-id.html) | [jvm]<br>val [spigotId](../../me.racci.raccicore.api.plugin/-racci-plugin/spigot-id.html): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0<br>The ID of your plugin on Spigot. |

