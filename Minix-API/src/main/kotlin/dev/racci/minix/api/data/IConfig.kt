package dev.racci.minix.api.data

import kotlin.reflect.KProperty

abstract class IConfig {

    // TODO: Find a way to make this work
    /**
     * Called when a value is updated.
     *
     * @param kPop The property that was updated.
     * @param value The previous value.
     */
    open fun updateCallback(kPop: KProperty<*>, value: Any?) {}

    /** A unit that is ran when this config is loaded. */
    open fun loadCallback() {}

    /** A unit that is ran when this config is unloaded. */
    open fun unloadCallback() {}
}
