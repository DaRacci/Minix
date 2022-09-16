package dev.racci.minix.api.utils.kotlin

@Deprecated("Use the new API", ReplaceWith("LazyUtil.ifInitialized(this, action)", "dev.racci.minix.api.reflection.LazyUtil"))
fun <T> Lazy<T>.ifInitialized(action: T.() -> Unit) {
    if (isInitialized()) action(value)
}
