@file:Suppress("unused")
@file:JvmName("YAMLConfig")
package me.racci.raccilib.config

import com.okkero.skedule.SynchronizationContext
import com.okkero.skedule.schedule
import me.racci.raccilib.Level
import me.racci.raccilib.log
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Entity
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.IOException
import kotlin.reflect.KClass

class YAMLConfig(private val plugin: Plugin, private val fileName: String) {

    /**
     * The [java.io.File] representation of this instance.
     */
    private var configFile: File

    /**
     * The [FileConfiguration] representation of this instance.
     */
    private lateinit var config: FileConfiguration

    init {
        if (fileName == "testingUseOnlyDoNotUseThisAsAnInputPlease") {
            configFile = File.createTempFile("config", ".yml")
            configFile.writeText("""
            |integer: 0
            |double: 0.2
            |string: 'this is a string'
            |boolean: true
            |color:
            |  ==: Color
            |  RED: 12
            |  GREEN: 42
            |  BLUE: 100
            |vector:
            |  ==: Vector
            |  x: 3.0
            |  y: 2.0
            |  z: 9.0
            |listOfInts:
            |  - 3
            |  - 2
            |testObject:
            |  integer: 5
            |  double: 0.5
            |  string: 'this is a string as well'
            |  boolean: false
            |  listOfInts:
            |    -  4
            |    -  78
            |    -  123
            """.trimMargin())
        } else {
            configFile = File(plugin.dataFolder, fileName)
        }

        saveDefault()
        reload()
    }

    /**
     * Gets the value at a specified [path], null if none.
     */
    operator fun get(path: String): Any? = config[path, null]

    /**
     * Gets the value at [path], [def] if none.
     */
    operator fun <T> get(path: String, def: T): T {
        return config[path, def] as T
    }

    /**
     * Gets the value at [path] with return type [T].
     */
    operator fun <T : Any> get(path: String, type: KClass<T>): T {
        return config[path, null] as T
    }

    /**
     * Sets the value at [path] to [value]
     */
    operator fun set(path: String, value: Any?): YAMLConfig {
        config[path] = value
        return this
    }

    @Deprecated("Seems like a waste...", level = DeprecationLevel.HIDDEN)
    operator fun unaryPlus(): YAMLConfig {
        saveDefault()
        save()
        return this
    }

    /**
     * Reloads the config from file.
     */
    fun reload(): Unit {
        config = YamlConfiguration.loadConfiguration(configFile)

        // Look for defaults in the jar
        val defaults = plugin.getResource(fileName)
        if (defaults != null) {
            config.defaults?.setDefaults(YamlConfiguration.loadConfiguration(defaults.reader()))
        }
    }

    /**
     * Saves the config to file.
     */
    fun save(): Unit {
        try {
            config.save(configFile)
        } catch (ex: IOException) {
            log(Level.ERROR, "Could not save config to $configFile")
        }
    }

    /**
     * Saves the default values of the config if they are not specified.
     */
    fun saveDefault() {
        if (!configFile.exists()) {
            plugin.saveResource(fileName, false)
        }
    }

    /**
     * String representation of the config.
     */
    override fun toString(): String {
        return config.toString()
    }
}