package dev.racci.minix.api.extension

import dev.racci.minix.api.events.PlatformListener
import dev.racci.minix.api.lifecycles.Closeable
import dev.racci.minix.api.lifecycles.ComplexManagedLifecycle
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.WithPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import org.apiguardian.api.API
import org.koin.core.annotation.Scope
import org.koin.core.annotation.Scoped
import org.koin.core.qualifier.Qualifier

@Scoped
@Scope(Extension::class)
public interface ExtensionSkeleton<P : MinixPlugin> : WithPlugin<P>, Qualifier, ComplexManagedLifecycle {

    public val dispatcher: Closeable<ExecutorCoroutineDispatcher>

    /** The Listener, which is used to register events in the extension. */
    @get:API(status = API.Status.INTERNAL)
    public val eventListener: PlatformListener<P>

    /** If the extension is loaded. */
    public val loaded: Boolean

    /** The name of the extension. */
    public val name: String

    /** The current state of the extension. */
    public val state: ExtensionState

    /**
     * The supervisor scope, which is used to launch coroutines in the
     * extension.
     */
    public val supervisor: CoroutineScope
}
