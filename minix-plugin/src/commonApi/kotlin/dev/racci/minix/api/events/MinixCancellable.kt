package dev.racci.minix.api.events

public expect interface MinixCancellable {
    /**
     * If this event is cancelled.
     * This is named this way because of jvm conflicts with the bukkit setCancelled method.
     */
    public var actualCancelled: Boolean
}
