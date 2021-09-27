package me.racci.raccicore.utils

/**
 * Lang load exception
 *
 * @constructor
 *
 * @param message
 */
class LangLoadException(message: String) : Exception(message)

/**
 * Lang default file exception
 *
 * @constructor
 *
 * @param message
 */
class LangDefaultFileException(message: String) : Exception(message)

/**
 * Lang no version exception
 *
 * @constructor
 *
 * @param message
 */
class LangNoVersionException(message: String) : Exception(message)

/**
 * Lang update file exception
 *
 * @constructor
 *
 * @param message
 */
class LangUpdateFileException(message: String) : Exception(message)

/**
 * File validation exception
 *
 * @constructor
 *
 * @param message
 */
class FileValidationException(message: String) : Exception(message)

/**
 * Origin condition exception
 *
 * @constructor
 *
 * @param message
 */
class OriginConditionException(message: String) : Exception(message)