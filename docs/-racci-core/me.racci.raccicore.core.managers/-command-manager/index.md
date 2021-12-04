//[RacciCore](../../../index.md)/[me.racci.raccicore.core.managers](../index.md)/[CommandManager](index.md)

# CommandManager

[jvm]\
class [CommandManager](index.md)(plugin: [RacciCore](../../me.racci.raccicore.core/-racci-core/index.md)) : [KListener](../../me.racci.raccicore.api.extensions/-k-listener/index.md)&lt;[RacciCore](../../me.racci.raccicore.core/-racci-core/index.md)&gt;
, [LifecycleListener](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/index.md)&lt;[RacciCore](../../me.racci.raccicore.core/-racci-core/index.md)&gt;

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [invoke](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/invoke.md) | [jvm]<br>open suspend operator override fun [invoke](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/invoke.md)(event: [LifecycleEvent](../../me.racci.raccicore.api.lifecycle/-lifecycle-event/index.md))<br>Handles invoking the [Lifecycle](../../me.racci.raccicore.api.lifecycle/-lifecycle/index.md) |
| [onDisable](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-disable.md) | [jvm]<br>open suspend fun [onDisable](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-disable.md)()<br>Called when the plugin is disabled. |
| [onEnable](on-enable.md) | [jvm]<br>open suspend override fun [onEnable](on-enable.md)()<br>Called when the RacciPlugin is enabled by the server and ready. |
| [onLoad](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-load.md) | [jvm]<br>open suspend fun [onLoad](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-load.md)()<br>Called when the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md) begins early loading. |
| [onReload](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-reload.md) | [jvm]<br>open suspend fun [onReload](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-reload.md)()<br>Called when reloading the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md). |

## Properties

| Name | Summary |
|---|---|
| [commands](commands.md) | [jvm]<br>val [commands](commands.md): [HashMap](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;Command&gt;&gt; |
| [plugin](plugin.md) | [jvm]<br>open override val [plugin](plugin.md): [RacciCore](../../me.racci.raccicore.core/-racci-core/index.md) |
