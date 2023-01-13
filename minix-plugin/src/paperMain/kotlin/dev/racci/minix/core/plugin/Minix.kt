package dev.racci.minix.core.plugin

import dev.racci.minix.api.annotations.BStatsId
import dev.racci.minix.api.logger.LoggingLevel
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.KoinUtils
import dev.racci.minix.core.LoadListener
import dev.racci.minix.core.builders.ItemBuilderImpl
import dev.racci.minix.core.data.MinixActualConfig
import dev.racci.minix.core.logger.SentryProxy
import dev.racci.minix.core.logger.SlimJarProxy
import dev.racci.minix.core.plugin.DummyLoader.Companion.setValue
import dev.racci.minix.core.services.PluginServiceImpl
import dev.racci.minix.core.services.mapped.ExtensionMapper
import dev.racci.minix.jumper.MinixApplicationBuilder
import io.sentry.Sentry
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.runBlocking
import org.bukkit.plugin.Plugin
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.dsl.new
import org.koin.dsl.binds
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule
import java.lang.ref.WeakReference

@Module
@ComponentScan
@BStatsId(13706)
public actual class Minix actual constructor(private val initPlugin: WeakReference<Plugin>) : MinixPlugin() {
    private var sentryState = atomic(false)

    override fun onLoad() {
        runBlocking {
            startKoin()
            MinixApplicationBuilder.logger = SlimJarProxy(logger)
            get<PluginService>().loadPlugin(this@Minix)
        }
    }

    override suspend fun handlePostLoad() {
        this.startSentry()
    }

    override suspend fun handleEnable() {
        initPlugin.get()?.setValue("isEnabled", true)
        this.registerEvents(LoadListener())
    }

    override suspend fun handleReload() {
        this.startSentry()
    }

    @Suppress("ProtectedInFinal")
    protected actual suspend fun startKoin() {
        // TODO: Logger
        startKoin {
            allowOverride(false)
            printLogger(Level.INFO)
            defaultModule()
            modules(KoinUtils.pluginModule(this@Minix))
            module(true) {
                single { new(::PluginServiceImpl) } binds arrayOf(PluginServiceImpl::class, PluginService::class)
                single { new(::ExtensionMapper) }
                single { ItemBuilderImpl }
            }.also(::modules)
        }

        with(get<ExtensionMapper>()) {
            val psi = get<PluginServiceImpl>()

            registerMapped(psi::class, this@Minix)
            registerMapped(this::class, this@Minix)

            loadExtension(psi::class, setOf())
            loadExtension(this::class, setOf())
        }
    }

    @Suppress("ProtectedInFinal")
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
                options.isDebug = logger.isEnabled(LoggingLevel.DEBUG)
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
