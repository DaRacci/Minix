package dev.racci.minix.core.plugin

import dev.racci.minix.api.annotations.MappedPlugin
import dev.racci.minix.api.logger.LoggingLevel
import dev.racci.minix.api.logger.MinixLogger
import dev.racci.minix.api.paper.builders.ItemBuilderDSL
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.KoinUtils
import dev.racci.minix.core.MinixApplicationBuilder
import dev.racci.minix.core.MinixImpl
import dev.racci.minix.core.builders.ItemBuilderImpl
import dev.racci.minix.core.data.MinixActualConfig
import dev.racci.minix.core.loggers.KoinProxy
import dev.racci.minix.core.loggers.SentryProxy
import dev.racci.minix.core.loggers.SlimJarProxy
import dev.racci.minix.core.services.PluginServiceImpl
import dev.racci.minix.core.services.mapped.ExtensionMapper
import io.sentry.Sentry
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.runBlocking
import org.koin.core.component.get
import org.koin.dsl.bind
import org.koin.dsl.module

@MappedPlugin(13706)
public actual class Minix : MinixPlugin() {
    private var sentryState = atomic(false)

    override fun onLoad() {
        runBlocking {
            startKoin()
            MinixApplicationBuilder.logger = SlimJarProxy
            get<PluginService>().loadPlugin(this@Minix)
        }
    }

    override suspend fun handlePostLoad() {
        this.startSentry()
    }

    override suspend fun handleReload() {
        this.startSentry()
    }

    protected actual suspend fun startKoin() {
        org.koin.core.context.startKoin {
            this.modules(
                KoinUtils.getModule(this@Minix),
                module { single { PluginDependentMinixLogger.getLogger(get<MinixImpl>()) } bind MinixLogger::class }
            )

            this.logger(KoinProxy)

            this.modules(
                module {
                    single { ItemBuilderImpl } bind ItemBuilderDSL::class
                }
            )

            this.modules(
                KoinUtils.getModule(PluginServiceImpl(get())),
                KoinUtils.getModule(ExtensionMapper(get()))
            )
        }

        with(get<ExtensionMapper>()) {
            val psi = get<PluginServiceImpl>()

            registerMapped(psi, this@Minix)
            registerMapped(this, this@Minix)

            loadExtension(psi, mutableListOf())
            loadExtension(this, mutableListOf())
        }
    }

    protected actual suspend fun startSentry() {
        val isEnabled = get<MinixActualConfig>().sentryEnabled

        if (sentryState.compareAndSet(true, isEnabled)) {
            Sentry.close()
            return
        }

        if (sentryState.compareAndSet(false, isEnabled)) {
            Sentry.init { options ->
                options.dsn = "https://80dedb0e861949509a7ed845deaca185@o1112455.ingest.sentry.io/6147185"
                options.release = description.version
                options.isDebug = log.isEnabled(LoggingLevel.DEBUG)
                options.environment = if (version.isPreRelease) "pre-release" else "release"
                options.environment = "production"
                options.inAppIncludes += "dev.racci.minix"
                options.setLogger(SentryProxy)
                options.setBeforeSend { event, _ ->
                    event.setTag("TPS-AVG", server.tps.average().toString())
                    event.setTag("TPS-CUR", server.tps.last().toString())
                    event
                }
            }
            Sentry.configureScope { scope ->
                scope.setContexts("ID", get<MinixActualConfig>().serverUUID)
                scope.setContexts("IP", server.ip)
                scope.setContexts("Minecraft Version", server.minecraftVersion)
                scope.setContexts("Server Version", server.version)
                scope.setContexts("Server Fork", server.name)
            }
        }
    }
}
