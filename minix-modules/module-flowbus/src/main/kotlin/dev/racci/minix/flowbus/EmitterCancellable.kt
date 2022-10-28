package dev.racci.minix.flowbus

public interface EmitterCancellable {
    public var cancelled: Boolean

    public fun cancel()
}
