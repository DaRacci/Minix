package dev.racci.minix.api.coroutine.contract

interface WakeUpBlockService {

    /**
     * Enables or disables the server heartbeat hack.
     */
    var isManipulatedServerHeartBeatEnabled: Boolean

    /**
     * Reference to the primary server thread.
     */
    val primaryThread: Thread

    /**
     * Calls scheduler management implementations to ensure the
     * is not sleeping if a run is scheduled by blocking.
     */
    fun ensureWakeup()

    /**
     * Disposes the service.
     */
    fun dispose()
}
