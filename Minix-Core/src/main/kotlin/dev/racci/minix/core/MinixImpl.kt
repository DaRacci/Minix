package dev.racci.minix.core

import dev.racci.minix.api.builders.ItemBuilderDSL
import dev.racci.minix.api.coroutine.contract.CoroutineService
import dev.racci.minix.api.data.Config
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.services.DataService
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.loadModule
import dev.racci.minix.core.builders.ItemBuilderImpl
import dev.racci.minix.core.coroutine.impl.CoroutineServiceImpl
import dev.racci.minix.core.scheduler.CoroutineSchedulerImpl
import dev.racci.minix.core.services.CommandService
import dev.racci.minix.core.services.DataServiceImpl
import dev.racci.minix.core.services.ListenerService
import dev.racci.minix.core.services.PlayerServiceImpl
import dev.racci.minix.core.services.PluginServiceImpl
import dev.racci.minix.core.services.TimeService
import dev.racci.minix.core.services.UpdaterService
import io.sentry.Sentry
import io.sentry.protocol.User
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.KoinApplication
import org.koin.core.annotation.KoinReflectAPI
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.mp.KoinPlatformTools
import kotlin.time.Duration.Companion.seconds

class MinixImpl : Minix() {
    private val config by lazy { get<DataService>().get<Config>() }

    override val bindToKClass get() = Minix::class
    override val bStatsId by lazy { 13706 }

    override fun onLoad() {
        startKoin()
        get<PluginService>().loadPlugin(this)
    }

    override suspend fun handleLoad() {
        extensions {
            add(::DataServiceImpl)
            add(::UpdaterService)
        }
        logger.level = config.loggingLevel
    }

    @OptIn(KoinReflectAPI::class)
    override suspend fun handleEnable() {

        startSentry()

        loadModule { single { ItemBuilderImpl.Companion } bind ItemBuilderDSL::class }

        extensions {
            add(::CoroutineSchedulerImpl)
            add(::CommandService)
            add(::PlayerServiceImpl)
            add(::ListenerService)
            add(::TimeService)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun handleDisable() {
        GlobalScope.launch { delay(1.seconds); KoinPlatformTools.defaultContext().getOrNull()?.close() }
    }

    @OptIn(KoinReflectAPI::class)
    private fun startKoin() {
        startKoin(KoinApplication.init())
        loadModule {
            single { PluginServiceImpl(this@MinixImpl) } bind PluginService::class
            single { CoroutineServiceImpl() } bind CoroutineService::class
        }
    }

    @Suppress("UnstableApiUsage")
    private fun startSentry() {
        if (!config.sentryEnabled) return

        Sentry.init { options ->
            options.dsn = "https://80dedb0e861949509a7ed845deaca185@o1112455.ingest.sentry.io/6147185"
            options.release = description.version
            options.isDebug = log.debugEnabled
            options.setBeforeSend { event, _ ->
                event.setTag("TPS", server.tps.toString())
                event
            }
            options.environment = "production"
            options.inAppIncludes += "dev.racci"
        }
        Sentry.configureScope { scope ->
            scope.user = User().apply {
                ipAddress = server.ip
                id = config.serverUUID.toString()
            }
            scope.setContexts("Minecraft Version", server.minecraftVersion)
            scope.setContexts("Server Version", server.version)
            scope.setContexts("Server Fork", server.name)
        }
    }
}
