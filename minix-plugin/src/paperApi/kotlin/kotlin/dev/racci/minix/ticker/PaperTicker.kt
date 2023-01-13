package dev.racci.minix.ticker

import dev.racci.minix.api.data.Priority
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.api.utils.ticks
import dev.racci.minix.flowbus.receiver.EventReceiver
import dev.racci.minix.flowbus.subscribe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import org.bukkit.event.player.PlayerQuitEvent

// TODO -> Add player to queue only after requested
// TODO -> Don't run if there are no subscriptions
public class PaperTicker : Ticker(1.ticks), EventReceiver by getKoin().get() {

    override val tickerScope: CoroutineScope = CoroutineScope(SupervisorJob())
    override lateinit var tickerDispatcher: ExecutorCoroutineDispatcher
    override lateinit var subscriberDispatcher: ExecutorCoroutineDispatcher

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun handleInit() {
        tickerDispatcher = newSingleThreadContext("Ticker-Publisher")
        subscriberDispatcher = newFixedThreadPoolContext(4, "Ticker-Subscriber")

        subscribe<PlayerQuitEvent>(Priority.MONITOR, true) {
        }
    }

    override suspend fun handleShutdown() {
        tickerDispatcher.close()
        subscriberDispatcher.close()
    }
}
