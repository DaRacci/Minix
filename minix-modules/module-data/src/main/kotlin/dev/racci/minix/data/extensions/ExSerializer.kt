package dev.racci.minix.data.extensions // ktlint-disable filename

import dev.racci.minix.api.utils.kotlin.ifNotNull
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.serializer
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
public inline fun <reified T : Any> CompositeEncoder.encodeNonNull(
    descriptor: SerialDescriptor,
    index: Int,
    serializer: KSerializer<T>,
    value: T?
): Boolean {
    contract {
        returns(true) implies (value != null)
    }

    return value.ifNotNull { encodeSerializableElement(descriptor, index, serializer, this) }
}

public inline fun <reified T : Any> CompositeEncoder.encodeNonDefault(
    descriptor: SerialDescriptor,
    index: Int,
    value: T,
    default: T,
    serializer: KSerializer<T> = serializer()
) {
    if (value != default) {
        encodeSerializableElement(descriptor, index, serializer, value)
    }
}

@OptIn(ExperimentalContracts::class)
public inline fun <reified T : Any> CompositeEncoder.encodeIf(
    descriptor: SerialDescriptor,
    index: Int,
    value: T?,
    serializer: KSerializer<T> = serializer(),
    predicate: () -> Boolean
) {
    contract {
        callsInPlace(predicate, InvocationKind.AT_MOST_ONCE)
    }

    if (!predicate()) return
    requireNotNull(value) { "Value cannot be null when predicate is true" }
    encodeSerializableElement(descriptor, index, serializer, value)
}
