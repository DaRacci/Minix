package dev.racci.minix.api.exceptions

/** Thrown when something, which tried to get the plugin via reflection fails. */
public class MissingPluginException(message: String) : Exception(message)
