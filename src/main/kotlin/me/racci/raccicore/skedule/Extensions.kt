package me.racci.raccicore.skedule

import org.jetbrains.annotations.ApiStatus

@Deprecated("Deprecated in favour of MCCoroutine", ReplaceWith(""))
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")
suspend inline fun <T> BukkitSchedulerController.runWithContext(context: SynchronizationContext, block: BukkitSchedulerController.() -> T): T {
    val before = currentContext()
    if (before != context) {
        switchContext(context)
    }
    val r = block()
    if (before != context) {
        switchContext(before)
    }
    return r
}