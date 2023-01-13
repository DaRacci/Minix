package dev.racci.minix.api.extensions

import dev.racci.minix.api.plugin.WithPlugin
import org.koin.core.Koin

/**
 * Retrieves a koin property with the plugin's prefix.
 *
 * @param key The key to retrieve
 * @see [Koin.getProperty]
 */
public fun <T : Any> WithPlugin<*>.getProperty(
    key: String
): T? = this.getKoin().getProperty("${this.plugin.value}:$key")

/**
 * Retrieves a koin property with the plugin's prefix.
 *
 * @param key The key to retrieve
 * @param default The default value to return if the key is not found.
 * @see [Koin.getProperty]
 */
public fun <T : Any> WithPlugin<*>.getProperty(
    key: String,
    default: T
): T = this.getKoin().getProperty("${this.plugin.value}:$key", default)

/**
 * Sets a koin property with the plugin's prefix.
 *
 * @param key The key to set
 * @param value The value to set
 * @see [Koin.setProperty]
 */
public fun <T : Any> WithPlugin<*>.setProperty(
    key: String,
    value: T
): T {
    this.getKoin().setProperty("${this.plugin.value}:$key", value)
    return value
}

/**
 * Deletes a koin property with the plugin's prefix.
 *
 * @param key The key to delete
 * @see [Koin.deleteProperty]
 */
public fun WithPlugin<*>.deleteProperty(
    key: String
): Unit = this.getKoin().deleteProperty("${this.plugin.value}:$key")
