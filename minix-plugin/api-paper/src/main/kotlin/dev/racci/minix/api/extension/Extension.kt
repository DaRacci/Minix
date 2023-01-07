package dev.racci.minix.api.extension

import dev.racci.minix.api.coroutine.CoroutineSession
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.koin
import dev.racci.minix.flowbus.receiver.EventReceiver
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job

public actual abstract class Extension<P : MinixPlugin> :
    PlatformIndependentExtension<P>(),
    EventReceiver by koin.get() {

    actual final override val EventReceiver.exceptionHandler: CoroutineExceptionHandler
        get() = this.supervisorScope.coroutineContext[CoroutineExceptionHandler]!!

    actual final override val EventReceiver.supervisorScope: CoroutineScope
        get() = this.supervisorScope

    public final override fun sync(
        block: suspend CoroutineScope.() -> Unit
    ): Job = scope.get<CoroutineSession>().launch(coroutineSession.synchronousContext, supervisor, block = block)

    public final override suspend fun <R> deferredSync(
        block: suspend () -> R
    ): Deferred<R> = super.deferredSync(block)
}
