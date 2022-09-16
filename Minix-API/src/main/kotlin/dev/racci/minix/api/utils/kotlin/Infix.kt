package dev.racci.minix.api.utils.kotlin

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

/**
 * Adds two elements to a new [PersistentList].
 */
infix fun <T> T.and(
    other: T
): PersistentList<T> = persistentListOf(this, other)
