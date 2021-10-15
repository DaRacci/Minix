package me.racci.raccicore.controllers


//internal class CommandController(
//        override val plugin: RacciCore
//) : KListener<RacciCore> {
//
//    val commands = hashMapOf<String, MutableList<Command>>()
//
//    init {
//        event<PluginDisableEvent> {
//            commands.remove(plugin.name)?.forEach {
//                it.unregister()
//            }
//        }
//    }
//}