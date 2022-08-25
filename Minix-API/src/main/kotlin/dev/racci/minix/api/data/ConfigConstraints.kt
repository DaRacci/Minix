package dev.racci.minix.api.data

import dev.racci.minix.api.plugin.logger.MinixLogger
import dev.racci.minix.api.utils.UtilObject
import org.spongepowered.configurate.objectmapping.meta.Constraint

object ConfigConstraints : UtilObject by UtilObject {
    private const val SCOPE = "config.constraints"

    object Minix : Constraint<MinixConfig<*>.Minix> {

        override fun validate(value: MinixConfig<*>.Minix?) {
            if (value == null) return

            val upper = value.loggingLevel.uppercase()
            if (value.loggingLevel != upper) {
                value.loggingLevel = upper
            }

            val enum = enumValues<MinixLogger.LoggingLevel>().find { it.name == value.loggingLevel } ?: return value.plugin.log.warn(scope = SCOPE) { "Invalid logging level '${value.loggingLevel}', using '${MinixLogger.LoggingLevel.INFO.name}' instead." }
            value.plugin.log.setLevel(enum)
        }
    }
}
