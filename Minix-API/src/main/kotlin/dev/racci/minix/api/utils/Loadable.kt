package dev.racci.minix.api.utils

import dev.racci.minix.api.utils.kotlin.catchAndReturn

abstract class Loadable<T> {

    protected var initialized: Boolean = false

    var value: T? = null

    val failed: Boolean get() = value == null && initialized
    val loaded: Boolean get() = value != null && initialized
    val unloaded: Boolean get() = value != null && !initialized
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
     * @return
     */
    fun get(force: Boolean = false): T? {
        return when (state) {
            State.LOADED -> value!!
            State.FAILED -> null
            State.UNLOADED, State.LOADABLE -> { load(force); value }
        }
    }

    /**
     * Predicated if the value is able to be loaded.
     *
     * @return true if the value is able to be loaded, false otherwise.
     */
    protected open fun predicateLoadable(): Boolean = true

    /**
     * The function to be called when the value is to be loaded.
     */
    abstract fun doLoad(): T

    /**
     * The function to be called when the value is to be unloaded.
     */
    abstract fun doUnload()

    fun load(force: Boolean = false) {
        if (!force && (initialized || !predicateLoadable())) return

        initialized = true
        value = catchAndReturn<Throwable, T> { doLoad() }
    }

    fun unload() {
        if (loaded) {
            doUnload()
        }

        initialized = false
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
