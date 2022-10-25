package dev.racci.minix.flowbus

import dev.racci.minix.api.extensions.reflection.safeCast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

public interface IDispatcherProvider {
    public fun getDispatcher(): CoroutineDispatcher
}

internal class DefaultDispatcherProvider : IDispatcherProvider {
    override fun getDispatcher() = Dispatchers.Default
}

/**
 * This class is responsible for checking whether any environmental specific [IDispatcherProvider] is also implemented in project.
 * If so, the [IDispatcherProvider] implementations can decide what Dispatcher should [EventReceiver] use by default.
 */
internal object DispatcherProvider {

    private const val ASYNC_PLUGIN_DISPATCHER = "dev.racci.minix.core.coroutine.AsyncDispatcher"

    private val provider by lazy {
        val providerNames = listOf(ASYNC_PLUGIN_DISPATCHER)
        providerNames.firstNotNullOfOrNull(DispatcherProvider::forName).safeCast<IDispatcherProvider>() ?: DefaultDispatcherProvider()
    }

    private fun forName(name: String) = runCatching {
        Class.forName(name)
    }.getOrNull()?.getDeclaredConstructor()?.newInstance()

    fun get() = provider.getDispatcher()
}
