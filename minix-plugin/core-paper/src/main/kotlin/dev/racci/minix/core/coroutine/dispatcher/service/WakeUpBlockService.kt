package dev.racci.minix.core.coroutine.dispatcher.service

import dev.racci.minix.api.extensions.collections.findKCallable
import dev.racci.minix.api.extensions.collections.findKProperty
import dev.racci.minix.api.extensions.reflection.accessGet
import dev.racci.minix.api.extensions.server
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.minecraft.NMSUtils
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.locks.LockSupport
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

internal class WakeUpBlockService(private val plugin: MinixPlugin) {

    private var threadSupport: ExecutorService? = null
    private val craftSchedulerTickProperty by lazy { NMSUtils.getNMSClass("CraftScheduler").declaredMemberProperties.findKProperty<Int>("currentTick").tap { it.isAccessible = true }.orNull()!! }
    private val craftSchedulerHeartBeatFunction by lazy { NMSUtils.getNMSClass("CraftScheduler").declaredMemberFunctions.findKCallable("mainThreadHeartbeat").orNull()!! }
    private val primaryThread by lazy {
        runBlocking {
            NMSUtils.getNMSClass("CraftServer").declaredMemberProperties.findKProperty<Any>("console")
                .map { it.accessGet(server) }
                .mapNotNull { it::class.declaredMemberProperties.findKProperty<Thread>("serverThread").orNull() }
                .map { it.accessGet(it) }
                .fold(
                    { error("Couldn't get the server thread, unsupported version?") },
                    { it }
                )
        }
    }

    var isManipulatedServerHeartBeatEnabled = false

    fun ensureWakeup() {
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
            if (LockSupport.getBlocker(primaryThread) == null) return@submit

            val currentTick = craftSchedulerTickProperty.get(plugin.server.scheduler)
            craftSchedulerHeartBeatFunction.call(plugin.server.scheduler, currentTick)
        }
    }

    fun dispose() {
        threadSupport?.shutdown()
    }
}
