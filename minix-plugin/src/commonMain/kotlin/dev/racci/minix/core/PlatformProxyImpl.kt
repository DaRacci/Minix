package dev.racci.minix.core

import dev.racci.minix.api.PlatformProxy
import dev.racci.minix.api.plugin.MinixPlugin
import org.koin.core.annotation.Singleton

@Singleton([PlatformProxy::class])
internal expect class PlatformProxyImpl : PlatformProxy {

    override fun firstNonMinixPlugin(): MinixPlugin?

    internal fun initialize()

    internal fun shutdown()
}
