package dev.racci.minix.data.exceptions

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor

/**
 * If there was an error while serializing or deserializing, a [MinixConfig] will throw this exception.
 */
public class SerializationException : IllegalArgumentException {

    @OptIn(ExperimentalSerializationApi::class)
    internal constructor(
        descriptor: SerialDescriptor,
        index: Int
    ) : super("Invalid serializable value for key ${descriptor.getElementName(index)}".trimIndent())

    internal constructor(string: String) : super(string)
}
