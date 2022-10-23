package dev.racci.minix.api.collections.expiring

import dev.racci.minix.api.utils.now
import kotlinx.datetime.Instant
import kotlin.time.Duration

public class ExpirationNode<E>(
    public var element: E,
    public val expireIn: Duration
) {

    public var next: ExpirationNode<E>? = null
    public var previous: ExpirationNode<E>? = null

    public var onExpire: ExpirationCallback<E>? = null
    public val startTime: Instant = now()
}
