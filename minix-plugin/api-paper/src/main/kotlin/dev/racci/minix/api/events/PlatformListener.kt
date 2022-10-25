package dev.racci.minix.api.events

import dev.racci.minix.api.extensions.KListener
import dev.racci.minix.api.plugin.MinixPlugin

public actual data class PlatformListener<P : MinixPlugin> actual constructor(public actual override val plugin: P) : KListener<P>
