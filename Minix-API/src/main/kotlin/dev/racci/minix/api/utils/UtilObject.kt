package dev.racci.minix.api.utils

import dev.racci.minix.api.extensions.WithPlugin
import dev.racci.minix.api.plugin.Minix
import org.koin.core.component.get

internal interface UtilObject : WithPlugin<Minix> {
    override val plugin get() = get<Minix>()
}
