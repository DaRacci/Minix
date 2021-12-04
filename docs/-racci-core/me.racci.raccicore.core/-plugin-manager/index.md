//[RacciCore](../../../index.md)/[me.racci.raccicore.core](../index.md)/[PluginManager](index.md)

# PluginManager

[jvm]\
class [PluginManager](index.md)(plugin: [RacciCore](../-racci-core/index.md)) : [LifecycleListener](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/index.md)&lt;[RacciCore](../-racci-core/index.md)&gt;
, [KListener](../../me.racci.raccicore.api.extensions/-k-listener/index.md)&lt;[RacciCore](../-racci-core/index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [invoke](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/invoke.md) | [jvm]<br>open suspend operator override fun [invoke](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/invoke.md)(event: [LifecycleEvent](../../me.racci.raccicore.api.lifecycle/-lifecycle-event/index.md))<br>Handles invoking the [Lifecycle](../../me.racci.raccicore.api.lifecycle/-lifecycle/index.md) |
| [onDisable](on-disable.md) | [jvm]<br>open suspend override fun [onDisable](on-disable.md)()<br>Called when the plugin is disabled. |
| [onEnable](on-enable.md) | [jvm]<br>open suspend override fun [onEnable](on-enable.md)()<br>Called when the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md) is enabled by the server and ready. |
| [onLoad](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-load.md) | [jvm]<br>open suspend fun [onLoad](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-load.md)()<br>Called when the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md) begins early loading. |
| [onReload](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-reload.md) | [jvm]<br>open suspend fun [onReload](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-reload.md)()<br>Called when reloading the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md). |

## Properties

| Name | Summary |
|---|---|
| [plugin](plugin.md) | [jvm]<br>open override val [plugin](plugin.md): [RacciCore](../-racci-core/index.md) |
