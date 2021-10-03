package me.racci.raccicore.utils

import me.racci.raccicore.RacciPlugin


abstract class PluginDependent<T : RacciPlugin>(val plugin: T)