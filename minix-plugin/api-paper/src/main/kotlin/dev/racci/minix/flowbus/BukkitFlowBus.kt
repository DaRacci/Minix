package dev.racci.minix.flowbus

import dev.racci.minix.api.extensions.pm
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.core.plugin.Minix
import kotlinx.coroutines.flow.onSubscription
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import kotlin.reflect.KClass

public class BukkitFlowBus : FlowBus() {
    private val listener = object : Listener {}

    public override fun <T : Any> forEvent(clazz: KClass<out T>): RendezvousStateFlow<T?> {
        val isBukkitEvent = clazz.java.isAssignableFrom(Event::class.java)
        val flow = super.forEvent(clazz)

        if (!isBukkitEvent || flow.subscriptionCount.value > 0) return flow // Already setup

        flow.onSubscription {
            if (flow.subscriptionCount.value > 0) return@onSubscription // Already setup
            println("Setting up bukkit listener for $clazz")

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
