@file:Suppress("unused")

package dev.racci.minix.api.extensions

import dev.racci.minix.api.serializables.UUIDSerializer
import java.util.UUID

fun UUID.serializer(): UUIDSerializer = UUIDSerializer
