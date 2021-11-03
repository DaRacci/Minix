package me.racci.raccicore.runnables

import me.racci.raccicore.RacciPlugin

object RunnableManager {

    val registeredRunnables = HashMap<RacciPlugin, List<KotlinRunnable>>()

    fun run(plugin: RacciPlugin) {
        registeredRunnables[plugin]?.forEach{it.start()}
    }

    fun close(plugin: RacciPlugin) {
        registeredRunnables[plugin]?.forEach{it.cancel()}
    }

    fun remove(plugin: RacciPlugin) {
        registeredRunnables.remove(plugin)
    }

}