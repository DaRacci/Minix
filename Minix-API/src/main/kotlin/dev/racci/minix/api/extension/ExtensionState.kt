package dev.racci.minix.api.extension

/**
 * Extension states, which describe what state of loading/unloading an extension is currently in.
 **/
enum class ExtensionState {

    FAILED_LOADING,
    FAILED_ENABLING,
    FAILED_UNLOADING,
    FAILED_DEPENDENCIES,

    LOADED,
    LOADING,

    ENABLED,
    ENABLING,

    UNLOADED,
    UNLOADING,
}
