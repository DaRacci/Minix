package me.racci.raccicore.interfaces

interface IManager<T> {

    /**
     * This method should be used to populate any maps,
     * variables and other info needed within the Manager.
     */
    fun init()

    /**
     * This method should be used to clear maps,
     * lists and leave as little behind as possible.
     */
    fun close()
}