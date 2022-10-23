package dev.racci.minix.updater.providers

import java.lang.Exception

public class NotSuccessfullyQueriedException : Exception("The provider has not been queried successfully so far!")
