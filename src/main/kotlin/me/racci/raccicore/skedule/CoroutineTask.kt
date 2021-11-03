package me.racci.raccicore.skedule

import me.racci.raccicore.RacciPlugin
import org.bukkit.scheduler.BukkitTask
import org.jetbrains.annotations.ApiStatus
import kotlin.coroutines.resume
@Deprecated("Deprecated in favour of MCCoroutine", ReplaceWith(""))
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")
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