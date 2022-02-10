package dev.racci.minix.api.extension

/**
 * Extension states, which describe what state of loading/unloading an extension is currently in.
 **/
enum class ExtensionState {

    FAILED_LOADING,
    FAILED_UNLOADING,

    LOADED,
    LOADING,

    UNLOADED,
    UNLOADING,
}
