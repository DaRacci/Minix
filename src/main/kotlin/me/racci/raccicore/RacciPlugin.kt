package me.racci.raccicore

import me.racci.raccicore.utils.strings.colour
import java.io.ObjectInputFilter



/**
 * Create the new plugin.
 * All values of the constructor are nullable
 *
 * @param colour        The colour for console messages
 * @param prefix        The prefix for console messages
 * @param spigotId      The spigot ID for the plugin
 * @param bStatsId      The bStats ID for the plugin
 */
abstract class RacciPlugin(colour: String? = "",
                       prefix: String? = "",
                       spigotId: Int?,
                       bStatsId: Int?) : org.bukkit.plugin.java.JavaPlugin() {

    /**
     * The colour for console messages
     */
    var colour: String ; private set

    /**
     * The prefix for console messages
     */
    var prefix: String ; private set
    /**
     * The spigot resource ID
     */
    var spigotId: Int? = null ; private set
    /**
     * The bStats resource ID
     */
    var bStatsId: Int? = null ; private set
    /**
     * Loaded external hooks
     */
    var loadedHooks = HashSet<String>() ; private set
    /**
     * The Lang resource
     */
//    var lang: Lang? = null ; private set
    /**
     * The Config resource
     */
    var config: ObjectInputFilter.Config? = null ; private set
    /**
     * The console Logger
     */
    var Logger = null ; private set
    /**
     * If the plugin has an update available
     */
    var outDated = false ; private set

    init {
        this.colour = colour(colour)!!
        this.prefix = colour(prefix)!!
        this.spigotId = spigotId
        this.bStatsId = bStatsId
    }



}

