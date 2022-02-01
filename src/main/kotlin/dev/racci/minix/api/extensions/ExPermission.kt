@file:Suppress("UNUSED", "UNCHECKED_CAST")

package dev.racci.minix.api.extensions

import org.bukkit.permissions.Permissible

fun Permissible.anyPermission(
    vararg permissions: String
) = permissions.any(::hasPermission)

fun Permissible.allPermission(
    vararg permissions: String
) = permissions.all(::hasPermission)

fun Permissible.hasPermissionOrStar(
    permission: String
) = hasPermission(permission) || hasPermission(permission.replaceAfterLast('.', "*"))
