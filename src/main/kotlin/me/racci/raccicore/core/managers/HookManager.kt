@file:Suppress("UNUSED")
package me.racci.raccicore.core.managers

import me.racci.raccicore.api.extensions.pm
import me.racci.raccicore.api.lifecycle.LifecycleListener
import me.racci.raccicore.core.RacciCore
import me.racci.raccicore.core.RacciCore.Companion.log
import org.geysermc.floodgate.api.FloodgateApi
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class HookManager(
    override val plugin: RacciCore
): LifecycleListener<RacciCore> {

    override suspend fun onEnable() {

        val plugins = pm.plugins.map{it.description.name.lowercase()}

        if(plugins.contains("floodgate")) {
            floodgate = FloodgateApi.getInstance()
            log.info("Hooked Floodgate!")
        }

    }

    companion object {
        internal val floodgateCache = ConcurrentHashMap<UUID, UUID>()

        val floodgateEnabled get() = floodgate != null

        var floodgate: FloodgateApi? = null

    }


}