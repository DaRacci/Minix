package me.racci.raccicore.interfaces

interface IFactory<T> {

    fun init()

    fun reload()

    fun close()

}