package dev.racci.minix.api.wrappers

import dev.racci.minix.api.exceptions.WrappingException

public fun interface WrapperCompanion<T> {
    /**
     * Creates a wrapped object [T] from the platform obj.
     * @param obj The player object to wrap.
     * @return The wrapper.
     * @throws WrappingException If the [obj] is not the platforms' [T] type.
     */
    @Throws(WrappingException::class)
    public fun wrapped(obj: Any): T
}
