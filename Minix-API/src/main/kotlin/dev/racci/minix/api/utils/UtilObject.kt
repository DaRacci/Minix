package dev.racci.minix.api.utils

import dev.racci.minix.api.plugin.MinixPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface UtilObject : KoinComponent {

    val minix: MinixPlugin

    companion object : UtilObject {
        override val minix: MinixPlugin by inject()
    }
}
