package me.racci.raccicore.interfaces

interface IFactory<T> {

    /**
     * This method should be used to populate any maps,
     * variables and other info needed within the Factory.
     */
    fun init()

    /**
     * This method should be used to repopulate maps,
     * variables and other info with changes such as a Lang file change.
     */
    fun reload()

    /**
     * This method should be used to clear maps,
     * lists and leave as little behind as possible.
     */
    fun close()

}