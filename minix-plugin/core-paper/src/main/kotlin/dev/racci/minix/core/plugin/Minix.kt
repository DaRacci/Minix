package dev.racci.minix.core.plugin

import dev.racci.minix.api.annotations.MappedPlugin
import dev.racci.minix.api.logger.MinixLogger
import dev.racci.minix.api.paper.builders.ItemBuilderDSL
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.KoinUtils
import dev.racci.minix.core.MinixApplicationBuilder
import dev.racci.minix.core.MinixImpl
import dev.racci.minix.core.builders.ItemBuilderImpl
import dev.racci.minix.core.loggers.KoinProxy
import dev.racci.minix.core.loggers.SentryProxy
import dev.racci.minix.core.loggers.SlimJarProxy
import dev.racci.minix.core.services.PluginServiceImpl
import dev.racci.minix.core.services.mapped.ExtensionMapper
import io.sentry.Sentry
import kotlinx.coroutines.runBlocking
import org.koin.core.annotation.Scope
import org.koin.core.annotation.Scoped
import org.koin.core.annotation.Singleton
import org.koin.dsl.bind
import org.koin.dsl.module

@Scoped
@Singleton
@Scope(Minix::class)
@MappedPlugin(13706, Minix::class)
public actual class Minix : MinixPlugin() {

    override fun onLoad() {
        runBlocking {
            startKoin()
            MinixApplicationBuilder.logger = SlimJarProxy
        }
    }

    protected actual suspend fun startKoin() {
        startKoin {
            this.allowOverride(false)

            this.modules(
                KoinUtils.getModule(this@MinixImpl),
                module { single { PluginDependentMinixLogger.getLogger(get<MinixImpl>()) } bind MinixLogger::class }
            )

            this.logger(KoinProxy)

            this.modules(
                module {
                    single { ItemBuilderImpl.Companion } bind ItemBuilderDSL::class
                }
            )

            this.modules(
                KoinUtils.getModule(PluginServiceImpl(get())),
                KoinUtils.getModule(ExtensionMapper(get()))
            )
        }

        with(get<ExtensionMapper>()) {
            val psi = get<PluginServiceImpl>()

            registerMapped(psi, this@MinixImpl)
            registerMapped(this, this@MinixImpl)

            loadExtension(psi, mutableListOf())
            loadExtension(this, mutableListOf())
        }
    }

    actual protected fun startSentry() {
        if (!config.sentryEnabled) return

        Sentry.init { options ->
            options.dsn = "https://80dedb0e861949509a7ed845deaca185@o1112455.ingest.sentry.io/6147185"
            options.release = description.version
            options.isDebug = log.isEnabled(MinixLogger.LoggingLevel.DEBUG)
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
            scope.setContexts("ID", config.serverUUID)
            scope.setContexts("IP", server.ip)
            scope.setContexts("Minecraft Version", server.minecraftVersion)
            scope.setContexts("Server Version", server.version)
            scope.setContexts("Server Fork", server.name)
        }
    }
}
