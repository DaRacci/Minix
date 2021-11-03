//package me.racci.raccicore.hooks
//
//import me.racci.raccicore.RacciPlugin
//import me.racci.raccicore.utils.pm
//import org.bukkit.WorldCreator.name
//import org.bukkit.plugin.Plugin
//import java.util.*
//import java.util.concurrent.Callable
//import java.util.stream.Collectors
//
//
//class HookService(
//    val plugin: RacciPlugin
//) {
//
//    private val availableHooks = ArrayList<Callable<AbstractHookService<*>>>()
//
//    private val registeredHooks = HashMap<Callable<AbstractHookService<*>>, AbstractHookService<*>>()
//
//    fun init() {
//        val enabledPlugins: Set<String> = Arrays.stream(pm.plugins)
//            .map(Plugin::getName)
//            .map(String::uppercase)
//            .collect(Collectors.toSet())
//
//
//        add(listOf(
//            "PlaceholderAPI" to PlaceholderHook(),
//            "Lands" to LandsHook()
//        ))
//        HookEnums.values().filter{enabledPlugins.contains(it.name)}
//            .map(HookEnums::abstractHookService).forEach(availableHooks::add)
//    }
//
//    fun add(pairs: List<Pair<String,AbstractHookService<*>>>) {
//
//    }
//
//    fun close() {
//        registeredHooks.values.forEach{it.close()}
//        registeredHooks.clear()
//        availableHooks.clear()
//    }
//
//    fun getHookService(hookService: Callable<AbstractHookService<*>>): AbstractHookService<*>? {
//        registeredHooks.computeIfAbsent(hookService) {
//            if(!availableHooks.contains(hookService))
//        }
//    }
//
//
//}