package dev.racci.minix.api.utils.minecraft

import io.mockk.every
import io.mockk.mockkStatic
import org.bukkit.Bukkit
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class MCVersionTest {

    @Test
    fun `test currentVersion`() {

        mockkStatic(Bukkit::class)
        every { Bukkit.getVersion() } returns "git-Tentacles-\"a28aadc\" (MC: 1.18.2)"

        assertEquals(MCVersion.MC_1_18_2, MCVersion.currentVersion)
    }
}
