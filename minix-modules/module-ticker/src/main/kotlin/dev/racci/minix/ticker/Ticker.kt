package dev.racci.minix.ticker

import dev.racci.minix.api.utils.now
import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.AtomicLong
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Instant
import kotlin.time.Duration

/**
 * The base implementation of a Ticker.
 */
// TODO -> Sorted priority queue
// TODO -> Sorted listener queue
public abstract class Ticker protected constructor(public val tickInterval: Duration) {
    protected abstract val tickerScope: CoroutineScope
    protected abstract val tickerDispatcher: ExecutorCoroutineDispatcher
    protected abstract val subscriberDispatcher: ExecutorCoroutineDispatcher

    private val listenableFlow = MutableSharedFlow<TickingEntity<*>>()
    private val unlockChannel: Channel<Unit> = Channel(Channel.RENDEZVOUS)
    private lateinit var activeJob: Job

    private val tickingEntities = LinkedHashSet<TickingEntity<*>>()

    private val _tickCount: AtomicLong = atomic(0L)
    private val _lastTick: AtomicRef<Instant> = atomic(Instant.DISTANT_PAST)

    private val _isRunning: AtomicBoolean = atomic(false)
    private val _isPaused: AtomicBoolean = atomic(false)

    public val tickCount: Long get() = _tickCount.value
    public val lastTick: Instant get() = _lastTick.value

    public val isRunning: Boolean get() = _isRunning.value
    public val isPaused: Boolean get() = _isPaused.value

    protected open suspend fun handleInit(): Unit = Unit

    protected open suspend fun handleShutdown(): Unit = Unit

    protected fun appendTicking(entity: TickingEntity<*>) {
        if (!tickingEntities.add(entity)) return

        if (!_isPaused.value) {
            unlockChannel.trySend(Unit)
        }
    }

    protected fun removeTicking(entity: TickingEntity<*>) {
        tickingEntities.remove(entity)
    }

    protected fun pause() {
        _isPaused.value = true
    }

    protected fun resume() {
        _isPaused.value = false
        unlockChannel.trySend(Unit)
    }

    internal suspend fun init() {
        if (::activeJob.isInitialized) error("Ticker is already initialized!")

        this.handleInit()
        activeJob = initFlow()
    }

    internal suspend fun shutdown() {
        if (!::activeJob.isInitialized) error("Ticker is not initialized!")

        activeJob.cancel(CancellationException("Ticker shutdown"))
        this.handleShutdown()
    }

    private fun initFlow(): Job = flow {
        do {
            if (_isPaused.value || tickingEntities.isEmpty()) {
                unlockChannel.receive() // Wait for unlock
                _tickCount.addAndGet(((now() - lastTick) / tickInterval).toLong()) // Estimate ticks that have passed during pause
            }

            val currentTick = _tickCount.incrementAndGet()
            val lastTick = _lastTick.getAndSet(now())

            tickingEntities.asSequence()
                .filterNot { entity -> entity.isRunning } // The entity hasn't finished its last tick.
                .forEach { entity ->
                    entity.isRunning = true
                    entity.lastTick = currentTick
                    emit(entity)
                }

            delay((lastTick + tickInterval) - now()) // Get the fixed time instead of a possibly incorrect time.
        } while (_isRunning.value)
    }.flowOn(tickerDispatcher)
        .buffer(Channel.UNLIMITED)
        .onEach(listenableFlow::emit)
        .flowOn(subscriberDispatcher)
        .catch { e -> e.printStackTrace() }
        .onEach { entity -> entity.isRunning = false }
        .flowOn(tickerDispatcher)
        .launchIn(tickerScope)
}
