package me.racci.raccicore

import me.racci.raccicore.utils.strings.colour

class Log(prefix: String = "", var debugMode: Boolean = false) {
    private val p = if(prefix == "") prefix else prefix.plus(" ")

    fun info(message: String, thrown: Throwable? = null) {
        println(colour("$p&e[&lINFO&e] $message"))
        thrown?.printStackTrace()
    }

    fun success(message: String, thrown: Throwable? = null) {
        println(colour("$p&a[&lSUCCESS&a] $message"))
        thrown?.printStackTrace()
    }

    fun warning(message: String, thrown: Throwable? = null) {
        println(colour("$p&c[&lWARNING&c] $message"))
        thrown?.printStackTrace()
    }

    fun error(message: String, thrown: Throwable? = null) {
        println(colour("&4&l-------------------------------"))
        println("")
        println(colour("$p&4[&lERROR&4] $message"))
        thrown?.printStackTrace()
        println("")
        println(colour("&4&l-------------------------------"))
    }

    fun debug(message: String, thrown: Throwable? = null) {
        if(debugMode) {
            println(colour("$p&5[&lDEBUG&5] $message"))
            thrown?.printStackTrace()
        }
    }

}