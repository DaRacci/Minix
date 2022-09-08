package dev.racci.minix.core

import dev.racci.minix.api.annotations.MappedPlugin
import dev.racci.minix.api.annotations.MinixInternal
import dev.racci.minix.api.builders.ItemBuilderDSL
import dev.racci.minix.api.coroutine.contract.CoroutineService
import dev.racci.minix.api.data.PluginUpdater
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.logger.MinixLogger
import dev.racci.minix.api.services.DataService
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.updater.Version
import dev.racci.minix.api.updater.providers.GithubUpdateProvider
import dev.racci.minix.api.utils.loadModule
import dev.racci.minix.core.builders.ItemBuilderImpl
import dev.racci.minix.core.coroutine.impl.CoroutineServiceImpl
import dev.racci.minix.core.data.MinixConfig
import dev.racci.minix.core.services.PluginServiceImpl
import io.sentry.Sentry
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinApplication
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.mp.KoinPlatformTools
import kotlin.time.Duration.Companion.seconds

@OptIn(MinixInternal::class)
@MappedPlugin(13706, Minix::class)
class MinixImpl : Minix() {
    private val config by lazy { DataService.getService().get<MinixConfig>() }

    override val updater: PluginUpdater = PluginUpdater().apply {
        ignored += "MinixUpdater"
        ignored += "libraries"
        name = "Minix"
        providers += GithubUpdateProvider("DaRacci", "Minix")
    }

    override fun onLoad() {
        runBlocking {
            startKoin()
            startSentry()

            get<PluginService>().loadPlugin(this@MinixImpl)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun handleUnload() {
        GlobalScope.launch { delay(1.seconds); KoinPlatformTools.defaultContext().getOrNull()?.close() }
    }

    private suspend fun startKoin() {
        startKoin(KoinApplication.init())
        loadModule {
            single { log } bind MinixLogger::class
            single { CoroutineServiceImpl() } bind CoroutineService::class
            single { ItemBuilderImpl.Companion } bind ItemBuilderDSL::class
        }

        val service = PluginServiceImpl(this)
        service.loadExtension(service, mutableListOf())
    }

    private fun startSentry() {
        if (!config.sentryEnabled) return

        Sentry.init { options ->
            options.dsn = "https://80dedb0e861949509a7ed845deaca185@o1112455.ingest.sentry.io/6147185"
            options.release = description.version
            options.isDebug = log.isEnabled(MinixLogger.LoggingLevel.DEBUG)
            options.environment = if (Version(this@MinixImpl.description.version).isPreRelease) "pre-release" else "release"
            options.environment = "production"
            options.inAppIncludes += "dev.racci"
            options.setBeforeSend { event, _ ->
                event.setTag("TPS-AVG", server.tps.average().toString())
                event.setTag("TPS-CUR", server.tps.last().toString())
                event
            }
        }
        Sentry.configureScope { scope ->
            scope.setContexts("ID", config.serverUUID)
            scope.setContexts("IP", server.ip)
            scope.setContexts("Minecraft Version", server.minecraftVersion)
            scope.setContexts("Server Version", server.version)
            scope.setContexts("Server Fork", server.name)
        }
    }
}
