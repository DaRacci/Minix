package dev.racci.minix.data.exceptions

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor

public class SerializationException : IllegalArgumentException {

    @OptIn(ExperimentalSerializationApi::class)
    public constructor(
        descriptor: SerialDescriptor,
        index: Int
    ) : super("Invalid serializable value for key ${descriptor.getElementName(index)}".trimIndent())

    public constructor(string: String) : super(string)
}
