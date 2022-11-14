package dev.racci.minix.data.extensions // ktlint-disable filename

import dev.racci.minix.data.delegates.DoubleAccessWatcher
import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty

public fun Delegates.doubleAccessWatcher(): ReadOnlyProperty<Any?, Boolean> = DoubleAccessWatcher()
