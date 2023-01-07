package dev.racci.minix.core.coroutine

import dev.racci.minix.api.annotations.MappedExtension
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
    override fun create(): ExecutorCoroutineDispatcher {
        val threadCount = extension::class.findAnnotation<MappedExtension>()!!.threadCount
        return newFixedThreadPoolContext(threadCount, "${extension.value.substringAfter(':')}-Thread")
    }

    override fun onClose() {
        value.value?.close()
    }
}
