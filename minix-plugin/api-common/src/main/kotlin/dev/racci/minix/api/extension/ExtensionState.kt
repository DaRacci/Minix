package dev.racci.minix.api.extension

/**
 * Extension states, which describe what state of loading/unloading an extension is currently in.
 **/
public enum class ExtensionState {
    LOADED,
    LOADING,

    ENABLED,
    ENABLING,

    DISABLED,
    DISABLING,

    UNLOADED,
    UNLOADING;
}
