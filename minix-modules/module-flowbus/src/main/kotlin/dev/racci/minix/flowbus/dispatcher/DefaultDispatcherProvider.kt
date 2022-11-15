package dev.racci.minix.flowbus.dispatcher

import kotlinx.coroutines.Dispatchers

internal class DefaultDispatcherProvider : DispatcherFactory {
    override fun getDispatcher() = Dispatchers.Default
}
