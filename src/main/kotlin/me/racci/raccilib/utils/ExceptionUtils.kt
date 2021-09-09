@file:Suppress("unused")
@file:JvmName("ExceptionUtils")
package me.racci.raccilib.utils

class LangLoadException(message: String) : Exception(message)
class LangDefaultFileException(message: String) : Exception(message)
class LangNoVersionException(message: String) : Exception(message)
class LangUpdateFileException(message: String) : Exception(message)

class FileValidationException(message: String) : Exception(message)

class OriginConditionException(message: String) : Exception(message)