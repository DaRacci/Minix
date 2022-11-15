package dev.racci.minix.flowbus.dispatcher

import dev.racci.minix.api.extensions.reflection.safeCast

/**
 * This class is responsible for checking whether any environmental specific [DispatcherFactory] is also implemented in project.
 * If so, the [DispatcherFactory] implementations can decide what Dispatcher should [EventReceiver] use by default.
 */

internal object DispatcherProvider {

    private const val PAPER_PLUGIN_DISPATCHER = "dev.racci.minix.core.coroutine.BukkitDispatcherFactory"

    private val provider by lazy {
        val providerNames = listOf(PAPER_PLUGIN_DISPATCHER)
        providerNames.firstNotNullOfOrNull(DispatcherProvider::forName).safeCast<DispatcherFactory>() ?: DefaultDispatcherProvider()
    }

    private fun forName(name: String) = runCatching {
        Class.forName(name)
    }.getOrNull()?.getDeclaredConstructor()?.newInstance()

    fun get() = provider.getDispatcher()
}
