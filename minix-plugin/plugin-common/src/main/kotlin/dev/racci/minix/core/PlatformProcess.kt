package dev.racci.minix.core

internal expect class PlatformProcess {

    internal fun initialize()

    internal fun shutdown()
}
