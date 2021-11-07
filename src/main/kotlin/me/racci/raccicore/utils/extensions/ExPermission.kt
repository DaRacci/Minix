package me.racci.raccicore.utils.extensions

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