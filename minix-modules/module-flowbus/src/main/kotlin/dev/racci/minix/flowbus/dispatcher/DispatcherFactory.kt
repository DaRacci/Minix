package dev.racci.minix.flowbus.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

public interface DispatcherFactory {
    public fun getDispatcher(): CoroutineDispatcher
}
