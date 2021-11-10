package me.racci.raccicore.utils

import java.lang.reflect.Constructor

object ClassUtils {

    /**
     * Checks if a class exists
     *
     * @param className the class to check
     * @return weather the class exists or not
     */
    fun exists(className: String): Boolean {
        catch<ClassNotFoundException>({return false}) {
            Class.forName(className)
            return true
        }
        return false
    }

    fun <T> classConstructor(constructor: Constructor<T>, vararg args: Any?): T {
        return constructor.newInstance(*args)
    }


}