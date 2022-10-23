package dev.racci.minix.api.extension

/** Represents that an extension has had a state change. */
public data class ExtensionStateEvent(
    val extension: Extension<*>,
    val preState: ExtensionState,
    val newState: ExtensionState
)
