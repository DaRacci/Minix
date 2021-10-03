package me.racci.raccicore.skedule

import me.racci.raccicore.RacciPlugin
import org.bukkit.scheduler.BukkitTask
import kotlin.coroutines.resume

class CoroutineTask internal constructor(private val controller: BukkitSchedulerController) {

    val plugin: RacciPlugin
        get() = controller.plugin
    val currentTask: BukkitTask?
        get() = controller.currentTask
    val isSync: Boolean
        get() = controller.currentTask?.isSync ?: false
    val isAsync: Boolean
        get() = !(controller.currentTask?.isSync ?: true)

    fun cancel() {
        controller.resume(Unit)
    }
}