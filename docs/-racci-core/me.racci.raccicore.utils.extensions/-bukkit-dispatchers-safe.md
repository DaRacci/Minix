//[RacciCore](../../index.md)/[me.racci.raccicore.api.utils.extensions](index.md)/[BukkitDispatchersSafe](-bukkit-dispatchers-safe.md)

# BukkitDispatchersSafe

[jvm]\
val [WithPlugin](-with-plugin/index.md)&lt;*&gt;.[BukkitDispatchersSafe](-bukkit-dispatchers-safe.md): [PluginDispatcher](-plugin-dispatcher/index.md)

Returns a PluginDispatcher to be used to provide ASYNC and SYNC Coroutines Dispatchers from Bukkit. by Skedule.

Uses Bukkit's JavaPlugin.getProvidingPlugin to safe retrieve the actually JavaPlugin class.

[jvm]\
val Plugin.[BukkitDispatchersSafe](-bukkit-dispatchers-safe.md): [PluginDispatcher](-plugin-dispatcher/index.md)
