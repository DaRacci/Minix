package dev.racci.minix.api.exceptions

class LevelConversionException(from: Any) : RuntimeException("Couldn't convert $from to a LoggingLevel.")
