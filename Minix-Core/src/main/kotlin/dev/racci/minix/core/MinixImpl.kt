package dev.racci.minix.core

import dev.racci.minix.api.builders.ItemBuilderDSL
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.kotlin.ifInitialized
import dev.racci.minix.api.utils.loadModule
import dev.racci.minix.core.builders.ItemBuilderImpl
import dev.racci.minix.core.scheduler.CoroutineSchedulerImpl
import dev.racci.minix.core.services.CommandServicesImpl
import dev.racci.minix.core.services.ListenerService
import dev.racci.minix.core.services.PlayerServiceImpl
import dev.racci.minix.core.services.PluginServiceImpl
import dev.racci.minix.core.services.TimeService
import io.sentry.Sentry
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.koin.core.KoinApplication
import org.koin.core.annotation.KoinReflectAPI
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.single
import org.koin.mp.KoinPlatformTools
import java.util.logging.Level
import kotlin.time.Duration.Companion.seconds

class MinixImpl : Minix() {

    override val bStatsId by lazy { 13706 }
    override val adventure = lazy { BukkitAudiences.create(this) }

    override fun onLoad() {
        startKoin()
        logger.level = Level.ALL
    }

    @OptIn(KoinReflectAPI::class)
    override suspend fun handleEnable() {

        startSentry()

        loadModule { single { this@MinixImpl } bind Minix::class }

        loadModule { single<ItemBuilderImpl.Companion>() bind ItemBuilderDSL::class }

        extensions {
            add(::CoroutineSchedulerImpl)
            add(::CommandServicesImpl)
            add(::PlayerServiceImpl)
            add(::ListenerService)
            add(::TimeService)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun handleDisable() {
        adventure.ifInitialized { close() }
        GlobalScope.launch { delay(1.seconds); KoinPlatformTools.defaultContext().getOrNull()?.close() }
    }

    private fun startKoin() {
        startKoin(KoinApplication.init())
        loadModule { single { PluginServiceImpl(this@MinixImpl) } bind PluginService::class }
    }

    @Suppress("UnstableApiUsage")
    private fun startSentry() {
        if (this.config.getBoolean("EnableSentry", true)) {
            Sentry.init { options ->
                options.dsn = "https://80dedb0e861949509a7ed845deaca185@o1112455.ingest.sentry.io/6147185"
                options.release = description.version
                options.setBeforeBreadcrumb { breadcrumb, _ -> //                    if (breadcrumb.message == null || breadcrumb.message!!.contains("Ignore", true)) {
                    //                        return@setBeforeBreadcrumb null
                    //                    }
                    // Add some relevant information to the breadcrumb.
                    breadcrumb.data["Minecraft Version"] = server.minecraftVersion
                    breadcrumb.data["Server Version"] = server.version
                    breadcrumb.data["Server Fork"] = server.name
                    breadcrumb.data["TPS"] = server.tps
                    breadcrumb
                } // I only want my own errors not whatever else goes on
//                options.inAppIncludes += "dev.racci"
            }
        }
    }
}
