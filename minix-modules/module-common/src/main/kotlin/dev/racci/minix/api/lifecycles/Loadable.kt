package dev.racci.minix.api.lifecycles

import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.updateAndGet
import kotlinx.coroutines.runBlocking

/**
 * A slighter more complicated and more advanced version of [Closeable]
 *
 * @param T the type of the object to be loaded
 */
public abstract class Loadable<T> {
    protected val initialized: AtomicBoolean = atomic(false)
    protected val error: AtomicRef<Throwable?> = atomic(null)
    protected val value: AtomicRef<T?> = atomic(null)

    public val failed: Boolean get() = error.value != null
    public val loaded: Boolean get() = value.value != null && initialized.value
    public val unloaded: Boolean get() = !initialized.value
    public val state: State
        get() = when {
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
     * @return the value of this loadable if it is loaded, or null if it isn't loaded.
     */
    public fun get(force: Boolean = false): Result<T> {
        return when (this.state) {
            State.LOADED -> Result.success(this.value.value!!)
            State.FAILED -> Result.failure(this.error.value!!)
            State.UNLOADED, State.LOADABLE -> { runBlocking { load(force) } }
        }
    }

    /**
     * Predicated if the value is able to be loaded.
     *
     * @return true if the value is able to be loaded, false otherwise.
     */
    protected open fun predicateLoadable(): Boolean = true

    /** The function to be called when the value is to be loaded. */
    public abstract suspend fun onLoad(): T

    /** The function to be called when the value is to be unloaded. */
    public open suspend fun onUnload(value: T): Unit = Unit

    /**
     * Attempts to load the value of this loadable.
     * If force loaded, and the value is already loaded, it will run [unload] before getting a new instance.
     *
     * @param force If we should try to load the value even if it is already loaded.
     * @return A result with the new value or an exception if one was thrown.
     */
    public suspend fun load(force: Boolean = false): Result<T> {
        if (!force && (loaded)) return Result.success(value.value!!)
        if (runCatching { !predicateLoadable() }.getOrDefault(false)) return Result.failure(RuntimeException("Loadable is not loadable because of predicate."))

        val success = initialized.updateAndGet { previous ->
            if (previous) this.unload()

            val value = runCatching { onLoad() }
            value.fold(
                onSuccess = { this.value.lazySet(it); this.error.lazySet(null) },
                onFailure = { this.value.lazySet(null); this.error.lazySet(it) }
            )

            value.isSuccess
        }

        return when (success) {
            true -> Result.success(value.value!!)
            false -> Result.failure(error.value!!)
        }
    }

    /**
     * Attempts to unload the value of this loadable.
     *
     * @return True if the value was previously loaded, false otherwise.
     */
    public suspend fun unload(): Boolean {
        if (this.loaded) {
            this.get().onSuccess { onUnload(it) }
        }

        return this.initialized.getAndSet(false)
    }

    final override fun toString(): String = "Loadable(state=$state, value=$value, error=$error)"

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Loadable<*>

        if (initialized.value != other.initialized.value) return false
        if (error.value != other.error.value) return false
        if (value.value != other.value.value) return false

        return true
    }

    final override fun hashCode(): Int {
        var result = initialized.value.hashCode()
        result = 31 * result + (error.value?.hashCode() ?: 0)
        result = 31 * result + (value.value?.hashCode() ?: 0)
        return result
    }

    public companion object {
        public inline fun <reified T> of(crossinline loader: () -> T): Loadable<T> {
            return object : Loadable<T>() {
                override suspend fun onLoad(): T = loader()
            }
        }
    }

    public enum class State {
        /* The value has been loaded and is available */
        LOADED,

        /* The value was loaded previously but is not available anymore */
        UNLOADED,

        /* The value is not loaded yet */
        LOADABLE,

        /* The value attempted to be loaded but failed */
        FAILED
    }
}
