package dev.racci.minix.api.utils.kotlin

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use the new API", ReplaceWith("LazyUtil.ifInitialized(this, action)", "dev.racci.minix.api.reflection.LazyUtil"))
public fun <T> Lazy<T>.ifInitialized(action: T.() -> Unit) {
    if (isInitialized()) action(value)
}
