package dev.racci.minix.api.data

import kotlin.reflect.KProperty

abstract class IConfig {

    // TODO: Find a way to make this work
    /** A unit that is called whenever a property is modified. */
    val updateCallback: ((KProperty<*>, Any?) -> Unit)? = null

    /** A unit that is ran when this config is first loaded. */
    val loadCallback: (() -> Unit)? = null

    /** A unit that is ran when this config is unloaded. */
    val unloadCallback: (() -> Unit)? = null
}
