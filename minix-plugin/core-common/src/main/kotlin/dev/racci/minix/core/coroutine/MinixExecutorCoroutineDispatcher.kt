package dev.racci.minix.core.coroutine

import dev.racci.minix.api.annotations.Threads
import dev.racci.minix.api.extension.ExtensionSkeleton
import dev.racci.minix.api.lifecycles.Closeable
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.newFixedThreadPoolContext
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import kotlin.reflect.full.findAnnotation

@Factory([Closeable::class])
@OptIn(DelicateCoroutinesApi::class)
internal class MinixExecutorCoroutineDispatcher(
    @InjectedParam private val extension: ExtensionSkeleton<*>
) : Closeable<ExecutorCoroutineDispatcher>() {
    private var usingDefaultDispatcher = false

    override fun create(): ExecutorCoroutineDispatcher {
        val threadCount = extension::class.findAnnotation<Threads>()?.threads ?: 0
        if (threadCount == 0.toShort()) {
            usingDefaultDispatcher = true
            return extension.plugin.coroutineSession.context as ExecutorCoroutineDispatcher
        }

        return newFixedThreadPoolContext(threadCount.toInt(), "${extension.value.substringAfter(':')}-thread")
    }

    override fun onClose() {
        if (usingDefaultDispatcher) return
        value.value?.close()
    }
}
