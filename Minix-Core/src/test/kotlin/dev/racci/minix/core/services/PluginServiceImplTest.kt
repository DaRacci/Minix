package dev.racci.minix.core.services

import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.plugin.logger.MinixLogger
import dev.racci.minix.api.utils.kotlin.fromOrdinal
import io.mockk.mockk
import io.mockk.mockkClass
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.test.mock.MockProvider
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PluginServiceImplTest {

    @get:ExtendWith
    val mockProvider = MockProvider.register { clazz ->
        mockkClass(clazz)
    }

    @Test
    fun `returns all layered extensions`() {
        mockk<PluginServiceImpl>()
        mockk<Extension<*>>()
    }

    @Test
    fun fromLog4J() {
        val log = org.apache.logging.log4j.Level.ALL
        val ordinal = ((log.intLevel() / 100 % 10) - 1).coerceAtLeast(0)
        val level = Enum.fromOrdinal<MinixLogger.LoggingLevel>(ordinal)

        println(log.intLevel())
        println(ordinal)
        println(level)

        assertEquals(level, MinixLogger.LoggingLevel.TRACE)
//                return when (log) {
//                    org.apache.logging.log4j.Level.ALL, org.apache.logging.log4j.Level.TRACE -> TRACE
//                    org.apache.logging.log4j.Level.DEBUG -> DEBUG
//                    org.apache.logging.log4j.Level.INFO -> INFO
//                    org.apache.logging.log4j.Level.WARN -> WARN
//                    org.apache.logging.log4j.Level.ERROR -> ERROR
//                    org.apache.logging.log4j.Level.FATAL, org.apache.logging.log4j.Level.OFF -> FATAL
//                    else -> throw LevelConversionException(log)
//                }
    }
}
