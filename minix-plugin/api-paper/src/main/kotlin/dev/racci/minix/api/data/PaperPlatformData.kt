package dev.racci.minix.api.data

import dev.racci.minix.api.data.enums.LiquidType
import dev.racci.minix.api.utils.now
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.milliseconds

public class PaperPlatformData : PlatformPlayerData() {

    private val instantCache = arrayOfNulls<Instant>(5)

    private fun doubleOrAdd(index: Int): Boolean {
        val now = now()
        val instant = instantCache[index]

        when {
            instant == null || now - instant > DOUBLE_DELAY -> {
                instantCache[index] = now
            }
            else -> instantCache[index] = null
        }
        return instantCache[index] == null
    }

    public val isDoubleOffhand: Boolean get() = doubleOrAdd(0)
    public val isDoubleSneak: Boolean get() = doubleOrAdd(1)
    public val isDoubleJump: Boolean get() = doubleOrAdd(2)
    public val isDoubleAttack: Boolean get() = doubleOrAdd(3)
    public val isDoubleInteract: Boolean get() = doubleOrAdd(4)

    public var liquidType: LiquidType = LiquidType.NON

    @JvmInline
    public value class DoubleClickable(private val index: Int) {
        public companion object {
            public val OFFHAND: DoubleClickable = DoubleClickable(0)
            public val SNEAK: DoubleClickable = DoubleClickable(1)
            public val JUMP: DoubleClickable = DoubleClickable(2)
            public val ATTACK: DoubleClickable = DoubleClickable(3)
            public val INTERACT: DoubleClickable = DoubleClickable(4)
        }
    }

    internal companion object {
        val DOUBLE_DELAY = 500.milliseconds
    }
}
