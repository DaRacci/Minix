@file:Suppress("UNUSED")

package dev.racci.minix.core

import dev.racci.minix.api.builders.ItemBuilderDSL
import dev.racci.minix.api.coroutine.asyncDispatcher
import dev.racci.minix.api.coroutine.launch
import dev.racci.minix.api.coroutine.launchAsync
import dev.racci.minix.api.coroutine.minecraftDispatcher
import dev.racci.minix.api.coroutine.scope
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.CommandServices
import dev.racci.minix.api.services.PlayerService
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.core.builders.ItemBuilderImpl
import dev.racci.minix.core.commands.CoreCommand
import dev.racci.minix.core.services.CommandServicesImpl
import dev.racci.minix.core.services.ListenerService
import dev.racci.minix.core.services.PlayerServiceImpl
import dev.racci.minix.core.services.PluginServiceImpl
import dev.racci.minix.core.services.TimeService
import dev.racci.minix.platforms.MinixPlatforms
import io.sentry.Sentry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.bukkit.NamespacedKey
import org.koin.core.annotation.KoinReflectAPI
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.dsl.single
import kotlin.collections.set

class Minix : MinixPlugin() {

    override suspend fun handleLoad() {
        Dispatchers.Main
        MinixPlatforms.load(this, "racci")
        if (this.config.getBoolean("EnableSentry", true)) {
            Sentry.init { options ->
                options.dsn = "https://80dedb0e861949509a7ed845deaca185@o1112455.ingest.sentry.io/6147185"
                options.release = description.version
                options.setBeforeBreadcrumb { breadcrumb, _ ->
                    if (breadcrumb.message == null || breadcrumb.message!!.contains("Ignore", true)) {
                        return@setBeforeBreadcrumb null
                    }
                    // Add some relevant information to the breadcrumb.
                    breadcrumb.data["Minecraft Version"] = server.minecraftVersion
                    breadcrumb.data["Server Version"] = server.version
                    breadcrumb.data["Server Fork"] = server.name
                    breadcrumb.data["TPS"] = server.tps
                    breadcrumb
                }
                // I only want my own errors not whatever else goes on
                options.inAppIncludes += "dev.racci"
            }
        }
    }

    @OptIn(KoinReflectAPI::class)
    override suspend fun handleEnable() {
        val module = module {
            single<ItemBuilderImpl.Companion>() bind ItemBuilderDSL::class
            single<CommandServicesImpl>() bind CommandServices::class
            single<PlayerServiceImpl>() bind PlayerService::class
            single<PluginServiceImpl>() bind PluginService::class
            single { this@Minix } bind Minix::class
        }

        startKoin {
            modules(module)
        }

        extensions {
            add(::ListenerService)
            add(::TimeService)
        }

        commands {
            add(::CoreCommand)
        }
    }

    internal companion object : KoinComponent {

        private val instance by inject<Minix>()
        val scope get() = instance.scope
        val asyncDispatcher get() = instance.asyncDispatcher
        val syncDispatcher get() = instance.minecraftDispatcher

        fun namespacedKey(value: String) = NamespacedKey(instance, value)
        fun launch(f: suspend CoroutineScope.() -> Unit) = instance.launch(f)
        fun launchAsync(f: suspend CoroutineScope.() -> Unit) = instance.launchAsync(f)
    }
}
