package dev.racci.minix.api.extensions

import org.bukkit.permissions.Permissible

// TODO -> Platform independent

public fun Permissible.anyPermission(
    vararg permissions: String
): Boolean = permissions.any(::hasPermission)

public fun Permissible.allPermission(
    vararg permissions: String
): Boolean = permissions.all(::hasPermission)

public fun Permissible.hasPermissionOrStar(
    permission: String
): Boolean = hasPermission(permission) || hasPermission(permission.replaceAfterLast('.', "*"))
