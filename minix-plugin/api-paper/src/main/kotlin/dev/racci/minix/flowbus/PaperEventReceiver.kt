package dev.racci.minix.flowbus

import dev.racci.minix.flowbus.receiver.EventReceiverImpl
import org.bukkit.event.Cancellable
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam

@Factory
public class PaperEventReceiver(@InjectedParam bus: PaperFlowBus) : EventReceiverImpl(bus) {
    override fun isCancelled(event: Any): Boolean {
        return ((event is Cancellable) && event.isCancelled) || super.isCancelled(event)
    }
}
