package dev.racci.minix.core

import dev.racci.minix.api.PlatformProxy
import org.koin.core.annotation.Singleton

@Singleton([PlatformProxy::class])
internal expect class PlatformProxyImpl : PlatformProxy {

    internal fun initialize()

    internal fun shutdown()
}
