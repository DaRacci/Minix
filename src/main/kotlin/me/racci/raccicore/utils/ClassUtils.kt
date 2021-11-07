package me.racci.raccicore.utils

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


}