package dev.racci.minix.api.utils

import dev.racci.minix.api.utils.kotlin.catchAndReturn
import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic

/**
 * A slighter more complicated and more advanced version of [Closeable]
 *
 * @param T the type of the object to be loaded
 */
abstract class Loadable<T> {

    protected val initialized: AtomicBoolean = atomic(false)

    val value: AtomicRef<T?> = atomic(null)

    val failed: Boolean get() = value.value == null && initialized.value
    val loaded: Boolean get() = value.value != null && initialized.value
    val unloaded: Boolean get() = value.value != null && !initialized.value
    val state: State get() = when {
        unloaded -> State.UNLOADED
        loaded -> State.LOADED
        failed -> State.FAILED
        else -> State.LOADABLE
    }

    /**
     * Attempts to get the value of this loadable.
     * If it isn't loaded it will try using the [load] method.
     * If there is an exception thrown, or [predicateLoadable] returns false this will return null.
     *
     * @param force Should we ignore what [predicateLoadable] returns
     * @return the value of this loadable if it is loaded, or null if it isn't loaded
     */
    fun get(force: Boolean = false): T? {
        return when (state) {
            State.LOADED -> value.value
            State.FAILED -> null
            State.UNLOADED, State.LOADABLE -> { load(force); value.value }
        }
    }

    /**
     * Predicated if the value is able to be loaded.
     *
     * @return true if the value is able to be loaded, false otherwise.
     */
    protected open fun predicateLoadable(): Boolean = true

    /** The function to be called when the value is to be loaded. */
    abstract fun onLoad(): T

    /** The function to be called when the value is to be unloaded. */
    abstract fun onUnload()

    /**
     * Attempts to load the value of this loadable.
     *
     * @param force If we should try to load the value even if it is already loaded
     * @return The previous value if it was already loaded, the old value if it was loaded but forced, or null.
     */
    fun load(force: Boolean = false): T? {
        if (!force && (initialized.value || !predicateLoadable())) return value.value

        initialized.lazySet(true)
        return value.getAndSet(catchAndReturn<Throwable, T> { onLoad() }) ?: value.value
    }

    /**
     * Attempts to unload the value of this loadable.
     *
     * @return True if the value was previously loaded, false otherwise.
     */
    fun unload(): Boolean {
        if (loaded) {
            onUnload()
        }

        return initialized.getAndSet(false)
    }

    enum class State {
        /* The value is loaded and available */
        LOADED,
        /* The value was loaded previously but is not available anymore */
        UNLOADED,
        /* The value is not loaded yet */
        LOADABLE,
        /* The value attempted to be loaded but failed */
        FAILED
    }
}
