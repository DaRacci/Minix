package dev.racci.minix.core.plugin

import dev.racci.minix.api.plugin.MinixPlugin

public expect class Minix : MinixPlugin {

    protected suspend fun startKoin()

    protected suspend fun startSentry()
}
