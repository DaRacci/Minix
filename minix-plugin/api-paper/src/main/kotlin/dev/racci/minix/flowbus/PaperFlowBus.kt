package dev.racci.minix.flowbus

import dev.racci.minix.api.extensions.pm
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.logger.MinixLoggerFactory
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.core.plugin.Minix
import kotlinx.coroutines.flow.onSubscription
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.koin.core.annotation.Singleton
import kotlin.reflect.KClass

@[Singleton([PaperFlowBus::class, FlowBus::class])]
public class PaperFlowBus : FlowBus() {
    private val logger by MinixLoggerFactory
    private val listener = object : Listener {}

    public override fun <T : Any> forEvent(clazz: KClass<out T>): RendezvousStateFlow<T?> {
        val isBukkitEvent = clazz.java.isAssignableFrom(Event::class.java)
        val flow = super.forEvent(clazz)

        if (!isBukkitEvent || flow.subscriptionCount.value > 0) return flow // Already setup

        flow.onSubscription {
            if (flow.subscriptionCount.value > 0) return@onSubscription // Already setup
            logger.debug { "Registering event listener for ${clazz.simpleName}" }

            val plugin = getKoin().get<Minix>()

            pm.registerEvent(
                clazz.java.castOrThrow(),
                listener,
                EventPriority.LOWEST,
                { _, event ->
                    if (clazz.isInstance(event) && event as? T != null) {
                        post(event)
                    }
                },
                plugin,
                false
            )
        }

        return flow
    }
}
