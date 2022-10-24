package dev.racci.minix.api.events

public expect interface MinixCancellable {

    /** If this event is cancelled. */
    public var actualCancelled: Boolean
}
