package dev.racci.minix.flowbus

public class MutableEventCallbackBuilder internal constructor() {

    public var priority: Priority = Priority.DEFAULT

    public fun priority(priority: Priority): MutableEventCallbackBuilder {
        this.priority = priority

        return this
    }

    public inline fun <reified T> build(crossinline callback: (T) -> Unit): EventCallback<T> = object : EventCallback<T> {
        override val priority: Priority get() = this@MutableEventCallbackBuilder.priority
        override fun onEvent(event: T) = callback(event)
    }
}
