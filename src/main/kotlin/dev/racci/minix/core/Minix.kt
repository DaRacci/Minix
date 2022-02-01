package dev.racci.minix.core

import dev.racci.minix.api.builders.ItemBuilderDSL
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.loadModule
import dev.racci.minix.core.builders.ItemBuilderImpl
import dev.racci.minix.core.scheduler.CoroutineSchedulerImpl
import dev.racci.minix.core.services.CommandServicesImpl
import dev.racci.minix.core.services.ListenerService
import dev.racci.minix.core.services.PlayerServiceImpl
import dev.racci.minix.core.services.PluginServiceImpl
import dev.racci.minix.core.services.TimeService
import io.sentry.Sentry
import org.koin.core.KoinApplication
import org.koin.core.annotation.KoinReflectAPI
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.single
import java.util.logging.Level
import kotlin.collections.set

class Minix : MinixPlugin() {

    override fun onLoad() {
        startKoin()
        logger.level = Level.ALL
    }

    @OptIn(KoinReflectAPI::class)
    override suspend fun handleEnable() {

        startSentry()

        loadModule { single { this@Minix } bind Minix::class }

        loadModule { single<ItemBuilderImpl.Companion>() bind ItemBuilderDSL::class }

        extensions {
            add(::CoroutineSchedulerImpl)
            add(::CommandServicesImpl)
            add(::PlayerServiceImpl)
            add(::ListenerService)
            add(::TimeService)
        }
    }

    private fun startKoin() {
        startKoin(KoinApplication.init())
        loadModule { single { PluginServiceImpl(this@Minix) } bind PluginService::class }
    }

    private fun startSentry() {
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
//                options.inAppIncludes += "dev.racci"
            }
        }
    }
}
