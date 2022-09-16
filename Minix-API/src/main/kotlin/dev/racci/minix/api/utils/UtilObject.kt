package dev.racci.minix.api.utils

import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.logger.MinixLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject

interface UtilObject : KoinComponent {

    val minix: MinixPlugin

    val logger: MinixLogger get() = get()

    companion object : UtilObject {
        override val minix: MinixPlugin by inject()
    }
}
