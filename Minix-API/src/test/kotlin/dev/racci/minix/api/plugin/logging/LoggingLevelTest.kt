package dev.racci.minix.api.plugin.logging

import dev.racci.minix.api.plugin.logger.MinixLogger.LoggingLevel
import io.kotest.core.spec.style.FunSpec
import java.util.logging.Level
import kotlin.test.assertEquals

class LoggingLevelTest : FunSpec({

    test("Conversion from java.util.logging.Level") {
        for (level in listOf(Level.ALL, Level.FINEST)) {
            assertEquals(LoggingLevel.TRACE, LoggingLevel.fromJava(level))
        }

        for (level in listOf(Level.FINER, Level.FINE, Level.CONFIG)) {
            assertEquals(LoggingLevel.DEBUG, LoggingLevel.fromJava(level))
        }

        assertEquals(LoggingLevel.INFO, LoggingLevel.fromJava(Level.INFO))
        assertEquals(LoggingLevel.WARN, LoggingLevel.fromJava(Level.WARNING))
        assertEquals(LoggingLevel.ERROR, LoggingLevel.fromJava(Level.SEVERE))
        assertEquals(LoggingLevel.FATAL, LoggingLevel.fromJava(Level.OFF))
    }

    test("Conversion from org.slf4j.event.Level") {
        assertEquals(LoggingLevel.TRACE, LoggingLevel.fromSlF4(org.slf4j.event.Level.TRACE))
        assertEquals(LoggingLevel.DEBUG, LoggingLevel.fromSlF4(org.slf4j.event.Level.DEBUG))
        assertEquals(LoggingLevel.INFO, LoggingLevel.fromSlF4(org.slf4j.event.Level.INFO))
        assertEquals(LoggingLevel.WARN, LoggingLevel.fromSlF4(org.slf4j.event.Level.WARN))
        assertEquals(LoggingLevel.ERROR, LoggingLevel.fromSlF4(org.slf4j.event.Level.ERROR))
    }

    test("Conversion from org.apache.logging.log4j.Level") {
        for (level in listOf(org.apache.logging.log4j.Level.ALL, org.apache.logging.log4j.Level.TRACE)) {
            assertEquals(LoggingLevel.TRACE, LoggingLevel.fromLog4J(level))
        }

        assertEquals(LoggingLevel.DEBUG, LoggingLevel.fromLog4J(org.apache.logging.log4j.Level.DEBUG))
        assertEquals(LoggingLevel.INFO, LoggingLevel.fromLog4J(org.apache.logging.log4j.Level.INFO))
        assertEquals(LoggingLevel.WARN, LoggingLevel.fromLog4J(org.apache.logging.log4j.Level.WARN))
        assertEquals(LoggingLevel.ERROR, LoggingLevel.fromLog4J(org.apache.logging.log4j.Level.ERROR))
        assertEquals(LoggingLevel.FATAL, LoggingLevel.fromLog4J(org.apache.logging.log4j.Level.FATAL))
    }
})
