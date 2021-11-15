package me.racci.raccicore.command

import com.github.shynixn.mccoroutine.asyncDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import me.racci.raccicore.RacciPlugin
import me.racci.raccicore.command.Failure.defaultErrorHandler
import me.racci.raccicore.extensions.WithPlugin
import me.racci.raccicore.extensions.msg
import me.racci.raccicore.extensions.register
import me.racci.raccicore.utils.catch
import me.racci.raccicore.utils.collections.onlinePlayerMapOf
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.reflect.KClass

typealias ExecutorBlock<T> = suspend Executor<T>.() -> Unit
typealias TabCompleterBlock = TabCompleter.() -> List<String>
typealias CommandBuilderBlock = CommandDSL.() -> Unit

fun WithPlugin<*>.command(
    name: String,
    vararg aliases: String = emptyArray(),
    job: Job = SupervisorJob(),
    coroutineScope: CoroutineScope = CoroutineScope(job + plugin.asyncDispatcher),
    block: CommandBuilderBlock
) = plugin.command(name, *aliases, job = job, coroutineScope = coroutineScope, block = block)


inline fun RacciPlugin.command(
    name: String,
    vararg aliases: String = emptyArray(),
    job: Job = SupervisorJob(),
    coroutineScope: CoroutineScope = CoroutineScope(job + asyncDispatcher),
    block: CommandBuilderBlock
) = command(name, *aliases, plugin = this, job = job, coroutineScope = coroutineScope, block = block)

inline fun command(
    name: String,
    vararg aliases: String = emptyArray(),
    plugin: RacciPlugin,
    job: Job = SupervisorJob(),
    coroutineScope: CoroutineScope = CoroutineScope(job + plugin.asyncDispatcher),
    block: CommandBuilderBlock
) = CommandDSL(plugin, name, *aliases, job = job, coroutineScope = coroutineScope).apply(block).apply {
    register(plugin)
}

class Executor<E: CommandSender>(
    val sender: E,
    val label: String, //TODO WHAT?
    val args: Array<out String>, // TODO auto cast
    val command: CommandDSL,
    val scope: CoroutineScope // TODO what?
)

class TabCompleter(
    val sender: CommandSender,
    val alias: String,
    val args: Array<out String>
)

open class CommandDSL(
    val plugin: RacciPlugin,
    name: String,
    vararg aliases: String = emptyArray(),
    private var executor: ExecutorBlock<CommandSender>? = null,
    var errorHandler: ErrorHandler = defaultErrorHandler,
    val job: Job = SupervisorJob(),
    private val coroutineScope: CoroutineScope = CoroutineScope(job + plugin.asyncDispatcher)
): org.bukkit.command.Command(name.trim()) {

    private val executors = mutableMapOf<KClass<out CommandSender>, ExecutorBlock<CommandSender>>()
    private val jobsFromPlayers by lazy {plugin.onlinePlayerMapOf<Job>()}
    private var tabCompleter: TabCompleterBlock? = null

    var onlyInGameMessage = ""
    var cancelOnPlayerDisconnect = true
    val subCommands = mutableListOf<CommandDSL>()

    fun TabCompleter.default(
    ) = defaultTabComplete(sender, alias, args)

    fun errorHandler(
        handler: ErrorHandler
    ) {errorHandler = handler}

    open fun subCommandBuilder(
        name: String,
        vararg aliases: String = emptyArray()
    ) = CommandDSL(
        plugin,
        name,
        *aliases,
        errorHandler = errorHandler,
        job = job,
        coroutineScope = coroutineScope
    ).also {
        it.permission = this.permission
        it.usageMessage = this.usageMessage
        it.onlyInGameMessage = this.onlyInGameMessage
        it.permissionMessage(this.permissionMessage())
    }

    inline fun command(
        name: String,
        vararg aliases: String = emptyArray(),
        block: CommandBuilderBlock
    ) = subCommandBuilder(name, *aliases).apply(block).also(subCommands::add)

    open fun executor(
        block: ExecutorBlock<CommandSender>
    ) {executor = block}

    open fun executorPlayer(
        block: ExecutorBlock<Player>
    ) {genericExecutor(Player::class, block)}

    open fun tabComplete(
        block: TabCompleterBlock
    ) {tabCompleter = block}

    open fun <T : CommandSender> genericExecutor(
        clazz: KClass<T>,
        block: ExecutorBlock<T>
    ) {executors[clazz] = block as ExecutorBlock<CommandSender>}

    inline fun <reified T : CommandSender> genericExecutor(
        noinline block: ExecutorBlock<T>
    ) {genericExecutor(T::class, block)}

    private fun <T> MutableMap<KClass<out CommandSender>, T>.getByInstance(
        clazz: KClass<*>
    ) = entries.find { it.key::class.isInstance(clazz) }?.value

    override fun execute(
        sender: CommandSender,
        commandLabel: String,
        args: Array<out String>
    ): Boolean {
        if(!permission.isNullOrEmpty() && !sender.hasPermission(permission!!)) {
            sender.msg(permissionMessage()!!) // TODO WHY NULLABLE?????
        } else {
            if(subCommands.isNotEmpty()) {
                val subCommand = args.getOrNull(0)?.let { arg->
                    subCommands.find {
                        it.name.equals(arg, true)
                        || it.aliases.any{a->a.equals(arg, true)}
                    }
                }
                if(subCommand != null) {
                    subCommand.execute(sender, "$label ${args[0]}", args.sliceArray(1 until args.size))
                    return true
                }
            }
        }
        val genericExecutor = executors.getByInstance(sender::class)
        if(genericExecutor != null) {
            coroutineScope.launch {
                val executorModel = Executor(sender, label, args, this@CommandDSL, coroutineScope)
                treatFail(executorModel) {
                    genericExecutor.invoke(executorModel)
                }
            }
        } else {
            val playerExecutor = executors.getByInstance(Player::class)
            if(playerExecutor != null) {
                if(sender is Player) {
                    val playerJob = Job()
                    if(cancelOnPlayerDisconnect) jobsFromPlayers.put(sender, playerJob) {if(it.isActive) it.cancel()}
                    coroutineScope.launch(playerJob) {
                        val executorModel = Executor(sender, label, args, this@CommandDSL, coroutineScope)
                        treatFail(executorModel) {
                            playerExecutor.invoke(executorModel as Executor<CommandSender>)
                        }
                    }
                } else sender.sendMessage(onlyInGameMessage)
            } else {
                coroutineScope.launch {
                    val executorModel = Executor(sender, label, args, this@CommandDSL, coroutineScope)
                    treatFail(executorModel) {
                        executor?.invoke(executorModel)
                    }
                }
            }
        }
        return true
    }

    override fun tabComplete(
        sender: CommandSender,
        alias: String,
        args: Array<out String>
    ) = tabCompleter?.invoke(TabCompleter(sender, alias, args)) ?: defaultTabComplete(sender, alias, args)

    open fun defaultTabComplete(
        sender: CommandSender,
        alias: String,
        args: Array<out String>
    ): List<String> {
        return if(args.size > 1) {
            val subCommand = subCommands.find{it.name.equals(args.getOrNull(0), true)}
            subCommand?.tabComplete(sender, args[0], args.sliceArray(1 until args.size)) ?: emptyList()
        } else if(args.isNotEmpty() && subCommands.isNotEmpty()) {
            subCommands.filter{it.name.startsWith(args[0], true)}.map{it.name}
        } else super.tabComplete(sender, alias, args)
    }

    private suspend fun treatFail(
        executor: Executor<*>,
        block: suspend () -> Unit
    ) {
        catch<Failure.CommandFailureException>({
            catch<Throwable>({
                executor.errorHandler(it)
            }) {
                it.senderMessage?.also{executor.sender.msg(it)}
                it.execute()
            }
        }) {
            block()
        }
    }




    init {
        this.aliases = listOf(*aliases)
    }

}