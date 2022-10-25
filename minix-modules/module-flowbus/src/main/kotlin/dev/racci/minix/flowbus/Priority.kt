package dev.racci.minix.flowbus

@JvmInline
public value class Priority(public val value: Int) {
    public companion object {
        /** Priority ensures the initial mutation of the event. */
        public val INITIAL: Priority = Priority(0)

        /** The default priority. */
        public val DEFAULT: Priority = Priority(25)

        /** Priority ensures the final mutation state of the event. */
        public val FINAL: Priority = Priority(50)

        /** This priority will not be able to mutate the event and is only able to listen. */
        public val MONITOR: Priority = Priority(-1)

        public fun of(value: Int): Priority {
            return Priority(value.coerceIn(MONITOR.value, FINAL.value))
        }

        @Throws(NumberFormatException::class)
        public fun of(value: String): Priority = Priority(value.toInt())
    }
}
