package dev.racci.minix.api.configuration.constraint

import dev.racci.minix.api.plugin.logger.MinixLogger
import dev.racci.minix.api.utils.collections.CollectionUtils.find
import org.spongepowered.configurate.objectmapping.meta.Constraint
import org.spongepowered.configurate.serialize.SerializationException

object MinixConstraints {

    object LoggingLevel : Constraint<String> {
        override fun validate(value: String?) {
            if (value == null) throw SerializationException("LoggingLevel cannot be null.")
            enumValues<MinixLogger.LoggingLevel>().find(value, true, Enum<*>::name) ?: throw SerializationException("LoggingLevel $value is not valid.")
        }
    }
}
