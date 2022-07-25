package dev.racci.minix.core

import dev.racci.minix.api.annotations.MappedPlugin
import dev.racci.minix.api.builders.ItemBuilderDSL
import dev.racci.minix.api.coroutine.contract.CoroutineService
import dev.racci.minix.api.data.PluginUpdater
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixLogger
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.DataService
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.updater.Version
import dev.racci.minix.api.updater.providers.GithubUpdateProvider
import dev.racci.minix.api.utils.loadModule
import dev.racci.minix.api.utils.safeCast
import dev.racci.minix.core.builders.ItemBuilderImpl
import dev.racci.minix.core.coroutine.impl.CoroutineServiceImpl
import dev.racci.minix.core.data.MinixConfig
import dev.racci.minix.core.services.PluginServiceImpl
import io.sentry.Breadcrumb
import io.sentry.Sentry
import io.sentry.SentryLevel
import io.sentry.protocol.User
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.KoinApplication
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.mp.KoinPlatformTools
import java.util.logging.Level
import kotlin.time.Duration.Companion.seconds

@MappedPlugin(13706, Minix::class)
class MinixImpl : Minix() {
    private val config by lazy { get<DataService>().get<MinixConfig>() }

    override val updater: PluginUpdater = PluginUpdater().apply {
        ignored += "MinixUpdater"
        name = "Minix"
        providers += GithubUpdateProvider("DaRacci", "Minix")
    }

    override fun onLoad() {
        startKoin()
        if (Version(description.version).isPreRelease) logger.level = Level.ALL
        get<PluginService>().loadPlugin(this)
    }

    override suspend fun handleAfterLoad() {
        logger.level = config.loggingLevel
    }

    override suspend fun handleEnable() {
        startSentry()

        loadModule { single { ItemBuilderImpl.Companion } bind ItemBuilderDSL::class }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun handleDisable() {
        GlobalScope.launch { delay(1.seconds); KoinPlatformTools.defaultContext().getOrNull()?.close() }
    }

    private fun startKoin() {
        startKoin(KoinApplication.init())
        loadModule {
            single { log } bind MinixLogger::class
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
            options.environment = if (Version(this@MinixImpl.description.version).isPreRelease) "pre-release" else "release"
            options.setBeforeSend { event, _ ->
                event.setTag("TPS-AVG", server.tps.average().toString())
                event.setTag("TPS-CUR", server.tps.last().toString())
                event
            }
            options.environment = "production"
            options.inAppIncludes += "dev.racci"

            options.setBeforeBreadcrumb { breadcrumb, hint ->
                val plugin = hint.get("plugin").safeCast<MinixPlugin>() ?: return@setBeforeBreadcrumb breadcrumb
                breadcrumb.data["plugin"] = plugin.description.name
                breadcrumb.data["plugin_version"] = plugin.description.version

                when (breadcrumb.level) {
                    SentryLevel.DEBUG -> plugin.log.debug(sentryLog(breadcrumb))
                    SentryLevel.INFO -> plugin.log.info(sentryLog(breadcrumb))
                    SentryLevel.WARNING -> plugin.log.warn(sentryLog(breadcrumb))
                    SentryLevel.ERROR, SentryLevel.FATAL -> plugin.log.error(sentryLog(breadcrumb))
                    null -> {}
                }

                breadcrumb
            }
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

    @Suppress("UnstableApiUsage")
    private fun sentryLog(breadcrumb: Breadcrumb): String {
        val builder = StringBuilder(breadcrumb.category)
        builder.append(" | ")
        builder.append(breadcrumb.message)

        if (breadcrumb.data.isEmpty()) return builder.toString()

        builder.append(" | ")
        builder.append(breadcrumb.data.entries.joinToString(", ", "[ ", " ]") { "${it.key}: ${it.value}" })
        return builder.toString()
    }
}
