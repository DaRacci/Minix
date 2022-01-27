package dev.racci.minix.api.extension

import dev.racci.minix.api.extensions.WithPlugin
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.core.Minix
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KClass

abstract class Extension : KoinComponent, WithPlugin<MinixPlugin> {

    abstract val name: String

    open val minix by inject<Minix>()

    open val log get() = plugin.log

    open val dependencies: ImmutableList<KClass<out Extension>> = persistentListOf()

    open var state: ExtensionState = ExtensionState.UNLOADED

    open val loaded: Boolean get() = state == ExtensionState.LOADED

    abstract suspend fun handleEnable()

    open suspend fun handleSetup() {
        setState(ExtensionState.LOADING)
        log.info { "Running handleSetup() for $name" }

        @Suppress("TooGenericExceptionCaught")
        try {
            handleEnable()
        } catch (t: Throwable) {
            setState(ExtensionState.FAILED_LOADING)
            throw t
        }

        setState(ExtensionState.LOADED)
    }

    open suspend fun setState(state: ExtensionState) {
        minix.send(ExtensionStateEvent(this, state))
        this.state = state
    }

    open suspend fun handleUnload() {
        log.trace { "Unload function not overridden." }
    }

    open suspend fun doUnload() {
        var error: Throwable? = null

        setState(ExtensionState.UNLOADING)

        @Suppress("TooGenericExceptionCaught")
        try {
            this.handleUnload()
        } catch (t: Throwable) {
            error = t
            setState(ExtensionState.FAILED_UNLOADING)
        }

        if (error != null) {
            throw error
        }

        setState(ExtensionState.UNLOADED)
    }
}
