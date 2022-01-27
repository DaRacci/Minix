package dev.racci.minix.api.extension

data class ExtensionStateEvent(
    val extension: Extension,
    val state: ExtensionState,
)
