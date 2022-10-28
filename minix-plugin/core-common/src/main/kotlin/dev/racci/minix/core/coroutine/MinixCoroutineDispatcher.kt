package dev.racci.minix.core.coroutine // ktlint-disable filename

import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.extension.ExtensionSkeleton
import dev.racci.minix.api.lifecycles.Closeable
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.newFixedThreadPoolContext
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Scoped
import kotlin.reflect.full.findAnnotation

@Scoped
@Factory([Closeable::class])
@Suppress("FunctionName")
@OptIn(DelicateCoroutinesApi::class)
internal fun MinixExecutorCoroutineDispatcher(
    extension: ExtensionSkeleton<*>
): Closeable<ExecutorCoroutineDispatcher> = object : Closeable<ExecutorCoroutineDispatcher>() {
    override fun create(): ExecutorCoroutineDispatcher {
        val threadCount = extension::class.findAnnotation<MappedExtension>()!!.threadCount
        return newFixedThreadPoolContext(threadCount, "${extension.value.substringAfter(':')}-Thread")
    }

    override fun onClose() { value.value?.close() }
}
