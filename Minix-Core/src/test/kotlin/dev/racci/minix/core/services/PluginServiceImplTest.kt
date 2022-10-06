package dev.racci.minix.core.services

import dev.racci.minix.api.plugin.logger.MinixLogger
import dev.racci.minix.api.utils.kotlin.fromOrdinal
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

//    @Test
//    fun `returns all layered extensions`() {
//        mockk<PluginServiceImpl>()
//        mockk<Extension<*>>()
//    }

    @Test
    fun fromLog4J() {
        val log = org.apache.logging.log4j.Level.ALL
        val ordinal = ((log.intLevel() / 100 % 10) - 1).coerceAtLeast(0)
        val level = Enum.fromOrdinal<MinixLogger.LoggingLevel>(ordinal)

        println(log.intLevel())
        println(ordinal)
        println(level)

        assertEquals(level, MinixLogger.LoggingLevel.TRACE)
    }
}
