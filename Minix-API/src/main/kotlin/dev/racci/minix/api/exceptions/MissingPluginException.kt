package dev.racci.minix.api.exceptions

/** Thrown when something which tried to get the plugin via reflection fails. */
class MissingPluginException(message: String) : Exception(message)
