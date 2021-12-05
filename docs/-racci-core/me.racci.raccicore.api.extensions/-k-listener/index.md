---
title: KListener
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.extensions](../index.html)/[KListener](index.html)



# KListener



[jvm]\
interface [KListener](index.html)&lt;[T](index.html) : [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html)&gt; : Listener, [WithPlugin](../-with-plugin/index.html)&lt;[T](index.html)&gt;



## Properties


| Name | Summary |
|---|---|
| [plugin](../-with-plugin/plugin.html) | [jvm]<br>abstract val [plugin](../-with-plugin/plugin.html): [T](index.html) |


## Inheritors


| Name |
|---|
| [SimpleKListener](../-simple-k-listener/index.html) |
| [OnlinePlayerCollection](../../me.racci.raccicore.api.utils.collections/-online-player-collection/index.html) |
| [OnlinePlayerMap](../../me.racci.raccicore.api.utils.collections/-online-player-map/index.html) |
| [PluginManager](../../me.racci.raccicore.core/-plugin-manager/index.html) |
| [CommandManager](../../me.racci.raccicore.core.managers/-command-manager/index.html) |
| [PlayerManager](../../me.racci.raccicore.core.managers/-player-manager/index.html) |


## Extensions


| Name | Summary |
|---|---|
| [event](../event.html) | [jvm]<br>inline fun &lt;[T](../event.html) : Event&gt; [KListener](index.html)&lt;*&gt;.[event](../event.html)(priority: EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, noinline block: [T](../event.html).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))<br>fun &lt;[T](../event.html) : Event&gt; [KListener](index.html)&lt;*&gt;.[event](../event.html)(type: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](../event.html)&gt;, priority: EventPriority = EventPriority.NORMAL, ignoreCancelled: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, block: [T](../event.html).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |

