package dev.racci.minix.core.coroutine.service

import dev.racci.minix.api.coroutine.contract.WakeUpBlockService
import dev.racci.minix.api.coroutine.findClazz
import dev.racci.minix.api.extensions.server
import dev.racci.minix.api.plugin.MinixPlugin
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.locks.LockSupport

class WakeUpBlockServiceImpl(private val plugin: MinixPlugin) : WakeUpBlockService {

    private var threadSupport: ExecutorService? = null
    private val craftSchedulerClazz by lazy { plugin.findClazz("org.bukkit.craftbukkit.VERSION.scheduler.CraftScheduler") }
    private val craftSchedulerTickField by lazy { craftSchedulerClazz.getDeclaredField("tick").apply { isAccessible = true } }
    private val craftSchedulerHeartBeatMethod by lazy { craftSchedulerClazz.getDeclaredMethod("mainThreadHeartbeat", Int::class.java) }

    override var isManipulatedServerHeartBeatEnabled: Boolean = false

    override val primaryThread: Thread by lazy {
        val dediServer = plugin.findClazz("org.bukkit.craftbukkit.VERSION.CraftServer")
            .getDeclaredField("console")
            .apply { isAccessible = true }
            .get(server)
        dediServer::class.java.getDeclaredField("serverThread").get(dediServer) as Thread
    }

    override fun ensureWakeup() {
        if (!isManipulatedServerHeartBeatEnabled) {
            if (threadSupport != null) {
                threadSupport!!.shutdown()
                threadSupport = null
            }
            return
        }

        if (threadSupport == null) {
            threadSupport = Executors.newFixedThreadPool(1)
        }

        threadSupport!!.submit {
            val blockingCoroutine = LockSupport.getBlocker(primaryThread)

            if (blockingCoroutine != null) {
                val currentTick = craftSchedulerTickField.get(plugin.server.scheduler)
                craftSchedulerHeartBeatMethod.invoke(plugin.server.scheduler, currentTick)
            }
        }
    }

    override fun dispose() {
        threadSupport?.shutdown()
    }
}
