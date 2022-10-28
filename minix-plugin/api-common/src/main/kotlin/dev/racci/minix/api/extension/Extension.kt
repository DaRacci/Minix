package dev.racci.minix.api.extension

import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.DataService
import dev.racci.minix.flowbus.receiver.EventReceiver

/**
 * An Extension is a class, which is designed to basically act like it's own mini plugin.
 * With dependencies for other extensions and load states.
 *
 * @param P The owning plugin.
 * @see DataService
 */
public expect abstract class Extension<P : MinixPlugin>() : PlatformIndependentExtension<P>, EventReceiver
