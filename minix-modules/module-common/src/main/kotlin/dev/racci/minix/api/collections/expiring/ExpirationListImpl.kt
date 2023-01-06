package dev.racci.minix.api.collections.expiring

import dev.racci.minix.api.utils.now
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.TickerMode
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.runBlocking
import org.jetbrains.annotations.ApiStatus
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KMutableProperty1
import kotlin.time.Duration

/**
 * The implementation of a list as an [ExpirationCollection].
 *
 * @param E The type of element in the collection.
 */
@ApiStatus.Experimental
@ApiStatus.AvailableSince("5.0.0")
public class ExpirationListImpl<E> internal constructor(
    private val backgroundContext: CoroutineContext
) : ExpirationCollection<E> {
    private var builtTicker: ReceiveChannel<Unit>? = null
    private var head: ExpirationNode<E>? = null
    private var tail: ExpirationNode<E>? = null
    private var emptyCount: Byte = 0
    override var size: Int = 0; private set

    override fun isEmpty(): Boolean = size == 0

    override operator fun contains(element: E): Boolean = indexOf(element) > -1

    public override operator fun get(index: Int): E? = getNode(index)?.value

    public override fun first(): E? = head?.value

    public override fun last(): E? = tail?.value

    override fun addAll(
        expireIn: Duration,
        vararg elements: E,
        onExpire: ExpirationCallback<E>?
    ): Unit = elements.forEach { add(it, expireIn, onExpire) }

    override operator fun iterator(): MutableIterator<E> = object : MutableIterator<E> {
        private val nodeIterator = nodeIterator()

        override fun hasNext() = nodeIterator.hasNext()
        override fun next() = nodeIterator.next().value
        override fun remove() = nodeIterator.remove()
    }

    public override fun indexOf(element: E): Int {
        if (head == null) return -1
        var node = head
        var count = 0
        do {
            if (node?.value === element) {
                return count
            }
            node = node?.next
            count++
        } while (node != null)
        return -1
    }

    override fun containsAll(elements: Collection<E>): Boolean = elements.all { contains(it) }

    override fun clear(callCallbacks: Boolean) {
        if (callCallbacks) {
            val nodeIterator = nodeIterator()
            while (nodeIterator.hasNext()) {
                val next = nodeIterator.next()
                next.onExpire?.onExpire(next.value)
                nodeIterator.remove()
            }
        }

        head = null
        tail = null
        size = 0
    }

    public override fun add(
        element: E,
        expireIn: Duration,
        onExpire: ExpirationCallback<E>?
    ): Unit = insert(
        element,
        expireIn,
        onExpire,
        ::tail,
        ExpirationNode<E>::next
    )

    public override fun addFirst(
        element: E,
        expireIn: Duration,
        onExpire: ExpirationCallback<E>?
    ): Unit = insert(
        element,
        expireIn,
        onExpire,
        ::head,
        ExpirationNode<E>::previous
    )

    override fun addLast(
        element: E,
        expireIn: Duration,
        onExpire: ExpirationCallback<E>?
    ): Unit = add(element, expireIn, onExpire)

    private fun insert(
        element: E,
        expireIn: Duration,
        onExpire: ExpirationCallback<E>?,
        node: KMutableProperty0<ExpirationNode<E>?>,
        nodeReplace: KMutableProperty1<ExpirationNode<E>, ExpirationNode<E>?>
    ) {
        require(expireIn.isNegative()) { "expireIn must be greater then 0." }

        val newNode = ExpirationNode(element, expireIn, now(), onExpire)
        if (head == null) {
            head = newNode
            tail = newNode
        } else {
            val actualNode = node()
            if (actualNode != null) {
                nodeReplace.set(actualNode, newNode)
            }
            node.set(newNode)
        }

        size++
        generateTask()
    }

    override fun removeAt(index: Int): E? = getNode(index)?.also {
        removeNode(it)
    }?.value

    override fun remove(element: E): Boolean = getNodeByElement(element)?.let { true.apply { removeNode(it) } } ?: false

    override fun removeFirst(): E? {
        val next = head?.next
        val headElement = head?.value
        if (next == null) {
            tail = null
            head = null
        } else {
            head = next
            next.previous = null
        }
        return headElement
    }

    override fun removeLast(): E? {
        val previous = tail?.previous
        val tailElement = tail?.value
        if (previous == null) {
            tail = null
            head = null
        } else {
            tail = previous
            previous.next = null
        }
        return tailElement
    }

    private fun getNode(index: Int): ExpirationNode<E>? {
        if (index < 0 || index >= size) return null

        val mid = size / 2
        return if (index > mid) {
            getFromSpecificSide(index - mid, tail) { it?.previous }
        } else getFromSpecificSide(index, head) { it?.next }
    }

    private fun getNodeByElement(element: E): ExpirationNode<E>? {
        if (head == null) return null
        var node = head
        do {
            if (node?.value === element) {
                return node
            }
            node = node?.next
        } while (node != null)
        return null
    }

    private inline fun getFromSpecificSide(
        count: Int,
        start: ExpirationNode<E>?,
        next: (ExpirationNode<E>?) -> ExpirationNode<E>?
    ): ExpirationNode<E> {
        var index = 0
        var current = start
        while (index != count) {
            current = next(current)
            index++
        }
        return current!!
    }

    private fun nodeIterator(): MutableIterator<ExpirationNode<E>> = object : MutableIterator<ExpirationNode<E>> {

        private var current = head

        override fun hasNext(): Boolean {
            return current != null
        }

        override fun next(): ExpirationNode<E> {
            val aux = current
            current = current?.next
            return aux!!
        }

        override fun remove() {
            if (current != null) {
                removeNode(current!!)
            }
        }
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    private fun generateTask() {
        if (builtTicker != null) return

        builtTicker = ticker(
            delayMillis = 50,
            initialDelayMillis = 50,
            context = backgroundContext,
            mode = TickerMode.FIXED_DELAY
        )

        runBlocking(backgroundContext) {
            if (isEmpty()) {
                emptyCount++
            } else {
                emptyCount = 0
                val current = now()
                for (node in nodeIterator()) {
                    if (node.remainingTime(current) != Duration.ZERO) continue
                    node.onExpire?.onExpire(node.value)
                    removeNode(node)
                }
            }

            if (emptyCount > 9) {
                builtTicker?.cancel()
                builtTicker = null
            }
        }
    }

    private fun removeNode(node: ExpirationNode<E>) {
        if (node === head && node === tail) {
            head = null
            tail = null
        } else if (node === head) {
            head = node.next?.apply { previous = null }
        } else if (node === tail) {
            tail = node.previous?.apply { next = null }
        } else {
            node.apply {
                previous?.next = next
                next?.previous = previous
            }
        }
        size--
    }
}
