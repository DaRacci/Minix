package dev.racci.minix.api.updater.providers

import java.lang.Exception

class NotSuccessfullyQueriedException : Exception("The provider has not been queried successfully so far!")
