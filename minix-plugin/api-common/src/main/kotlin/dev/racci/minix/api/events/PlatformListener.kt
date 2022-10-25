package dev.racci.minix.api.events

import dev.racci.minix.api.plugin.MinixPlugin

public expect class PlatformListener<P : MinixPlugin> internal constructor(plugin: P) {
    public val plugin: P
}
