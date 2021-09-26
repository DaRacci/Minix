package me.racci.raccilib.skedule

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