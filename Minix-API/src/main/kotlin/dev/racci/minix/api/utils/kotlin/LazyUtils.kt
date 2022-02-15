package dev.racci.minix.api.utils.kotlin

fun <T> Lazy<T>.ifInitialized(action: T.() -> Unit) {
    if (isInitialized()) action(value)
}
