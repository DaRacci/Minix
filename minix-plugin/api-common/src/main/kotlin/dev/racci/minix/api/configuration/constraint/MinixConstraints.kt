package dev.racci.minix.api.configuration.constraint

import org.spongepowered.configurate.objectmapping.meta.Constraint
import org.spongepowered.configurate.serialize.SerializationException

public object MinixConstraints {

    public object LoggingLevel : Constraint<String> {
        override fun validate(value: String?) {
            if (value == null) throw SerializationException("LoggingLevel cannot be null.")
            enumValues<dev.racci.minix.api.logger.LoggingLevel>()
                .find { value.equals(it.name, true) } ?: throw SerializationException("LoggingLevel $value is not valid.")
        }
    }
}
