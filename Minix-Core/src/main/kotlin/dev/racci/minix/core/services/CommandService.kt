package dev.racci.minix.core.services

import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.plugin.Minix

@MappedExtension(Minix::class, "Command Service")
class CommandService(override val plugin: Minix) : Extension<Minix>()
