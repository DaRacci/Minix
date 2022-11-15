package dev.racci.minix.api.data

@JvmInline
public value class Priority internal constructor(
    public val value: Int
) : Comparable<Priority> {

    override fun compareTo(other: Priority): Int = value.compareTo(other.value)

    public companion object {
        private val VALUES = Array(Const.MONITOR.level.inv() + Const.FINAL.level) { Priority(it - 1) }

        public val INITIAL: Priority = Const.INITIAL.getPriority()

        public val DEFAULT: Priority = Const.DEFAULT.getPriority()

        public val FINAL: Priority = Const.FINAL.getPriority()

        public val MONITOR: Priority = Const.MONITOR.getPriority()

        public fun of(value: Int): Priority = VALUES[value.inc().coerceIn(0, VALUES.size)]

        @Throws(NumberFormatException::class)
        public fun of(value: String): Priority = this.of(value.toInt())
    }

    public enum class Const(internal val level: Int) {
        /** Priority ensures the initial mutation of the event. */
        INITIAL(0),

        /** The default priority. */
        DEFAULT(25),

        /** Priority ensures the final mutation state of the event. */
        FINAL(50),

        /** This priority will not be able to mutate the event and is only able to listen. */
        MONITOR(-1);

        public fun getPriority(): Priority {
            return of(level)
        }
    }
}
