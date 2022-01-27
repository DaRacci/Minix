package dev.racci.minix.api.plugin

import co.aikar.commands.BaseCommand
import co.aikar.commands.PaperCommandManager
import dev.racci.minix.api.annotations.MinixDsl
import dev.racci.minix.api.coroutine.coroutine
import dev.racci.minix.api.coroutine.launchAsync
import dev.racci.minix.api.coroutine.registerSuspendingEvents
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extension.ExtensionStateEvent
import dev.racci.minix.api.extension.InvalidExtensionException
import dev.racci.minix.api.extensions.pm
import dev.racci.minix.api.scheduler.CoroutineScheduler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import mu.KLogger
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus
import org.koin.core.component.KoinComponent
import kotlin.properties.Delegates
import kotlin.reflect.KClass

/**
 * The superclass replacing [JavaPlugin],
 * This class provides many more features and allows implementation into the
 * systems of Minix.
 */
@Suppress("LeakingThis")
open class MinixPlugin : SusPlugin(), KoinComponent {

    private val extensionsBuilder = ExtensionsBuilder()
    private val listenerBuilder = ListenerBuilder()
    private val commandBuilder = CommandBuilder()

    open val extensions: MutableMap<String, Extension> = mutableMapOf()
    val extensionEvents: MutableSharedFlow<Any> = MutableSharedFlow()

    open val log: KLogger = MinixLogger(this, slF4JLogger)
    open var commandManager by Delegates.notNull<PaperCommandManager>()

    // Add Update checker back for Spigot as well as adding support for GitHub, mc-market and polymart
    // Add BStats support
    // Possibly move this complete startup method to a service utility that runs inside of Minix itself
    @ApiStatus.Internal
    @ApiStatus.NonExtendable
    override fun onEnable() {
        coroutine.getCoroutineSession(this).wakeUpBlockService.isManipulatedServerHeartBeatEnabled = true
        runBlocking {
            log.info("Loading and enabling ${this@MinixPlugin.description.fullName}")

            commandManager = PaperCommandManager(this@MinixPlugin)

            log.info { "Running handleEnable() for ${this@MinixPlugin.name}" }
            handleEnable()

            log.info { "Registering Extensions for ${this@MinixPlugin.name}" }
            extensionsBuilder.extensions.forEach { addExtension(it) }
            log.info { "Executing Extensions for ${this@MinixPlugin.name}" }
            executeInOrder(ExtensionEvent.ENABLE, extensions.values)

            log.info { "Registering all listeners for ${this@MinixPlugin.name}" }
            listenerBuilder.listeners.forEach { pm.registerSuspendingEvents(it.invoke(), this@MinixPlugin) }
            commandBuilder.commands.forEach { commandManager.registerCommand(it.invoke()) }

            log.info("Finished loading ${this@MinixPlugin.name}, Thank you for using Minix!")

            log.info("Running handleAfterLoad() for ${this@MinixPlugin.name}")
            handleAfterLoad()
        }
        // Disables runBlocking hack to not interfere with other tasks.
        coroutine.getCoroutineSession(this).wakeUpBlockService.isManipulatedServerHeartBeatEnabled = false
    }

    @ApiStatus.Internal
    @ApiStatus.NonExtendable
    override fun onDisable() {
        runBlocking {
            CoroutineScheduler.cancelAllTasks(this@MinixPlugin)
            commandManager.unregisterCommands()
            handleDisable()
            executeInOrder(ExtensionEvent.DISABLE, extensions.values)
        }
    }

    @ApiStatus.Internal
    @ApiStatus.NonExtendable
    override fun onLoad() {
        runBlocking {
            log.debug { "Handle Load for ${this@MinixPlugin.name}" }
            handleLoad()
        }
    }

    @ApiStatus.Internal
    @ApiStatus.NonExtendable
    suspend fun reload() {
        launchAsync {
            log.debug { "Handle reload for ${this@MinixPlugin.name}" }
            handleReload()
            executeInOrder(ExtensionEvent.RELOAD, extensions.values)
        }
    }

    @ApiStatus.Internal
    @ApiStatus.NonExtendable
    suspend inline fun send(event: ExtensionStateEvent) {
        extensionEvents.emit(event)
    }

    @MinixDsl
    open suspend fun extensions(builder: suspend ExtensionsBuilder.() -> Unit) {
        builder(extensionsBuilder)
    }

    @MinixDsl
    open suspend fun listeners(builder: suspend ListenerBuilder.() -> Unit) {
        builder(listenerBuilder)
    }

    @MinixDsl
    open suspend fun commands(builder: suspend CommandBuilder.() -> Unit) {
        builder(commandBuilder)
    }

    @Throws(InvalidExtensionException::class)
    open suspend fun addExtension(builder: (MinixPlugin) -> Extension) {
        val extensionObj = builder.invoke(this)
        extensions[extensionObj.name] = extensionObj
    }

    private suspend fun executeInOrder(
        event: ExtensionEvent,
        extensions: Collection<Extension>,
    ) {
        val ordered = mutableListOf<Extension>()
        val loaded = mutableListOf<KClass<out Extension>>()
        // Time it out and error after 10 seconds
        withTimeout(10000) {
            fun addDependencies(extension: Extension) {
                for (dependency in extension.dependencies) {
                    if (dependency in loaded) continue
                    if (dependency !in extensions.map { println("Extension KClass -> ${it::class}"); it::class }) {
                        log.error {
                            "Could not resolve missing dependency $dependency required by extension ${extension.name}."
                        }
                    }
                    val ext = extensions.first { it::class == dependency }
                    if (ext.dependencies.all { it in loaded }) {
                        ordered += ext
                        loaded += ext::class
                        continue
                    }
                    addDependencies(ext)
                }
            }
            for (extension in extensions) {
                log.info { "Adding dependencies for ${extension.name}" }
                addDependencies(extension)
            }
        }
        val executeOrder = if (event == ExtensionEvent.ENABLE) {
            ordered
        } else ordered.asReversed()

        // TODO: Find how to call suspended methods from reflection
        for (extension in executeOrder) {
            if (event == ExtensionEvent.ENABLE) {
                executeOrder.forEach { it.handleSetup() }
            } else executeOrder.forEach { it.doUnload() }

            if (event == ExtensionEvent.RELOAD) ordered.forEach { it.handleSetup() }
        }
    }

    @MinixDsl
    open class ExtensionsBuilder {

        open val extensions: MutableList<(MinixPlugin) -> Extension> = mutableListOf()

        open fun add(builder: (MinixPlugin) -> Extension) {
            extensions.add(builder)
        }
    }

    @MinixDsl
    open class ListenerBuilder {

        open val listeners: MutableList<() -> Listener> = mutableListOf()

        open fun add(builder: () -> Listener) {
            listeners.add(builder)
        }
    }

    @MinixDsl
    open class CommandBuilder {

        open val commands: MutableList<() -> BaseCommand> = mutableListOf()

        open fun add(builder: () -> BaseCommand) {
            commands.add(builder)
        }
    }

    enum class ExtensionEvent {
        ENABLE,
        RELOAD,
        DISABLE,
    }
}
