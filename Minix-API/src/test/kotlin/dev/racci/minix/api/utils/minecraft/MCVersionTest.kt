package dev.racci.minix.api.utils.minecraft

import org.bukkit.Bukkit
import org.bukkit.Server
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import kotlin.test.assertEquals

internal class MCVersionTest {

    @Test
    fun `test currentVersion`() {

        val mock = Mockito.mockStatic(Bukkit::class.java)
        val mockServer = mock<Server>()

        mock.`when`<Server>(Bukkit::getServer).doReturn(mockServer)
        mock.`when`<String>(Bukkit::getVersion).doReturn("git-Tentacles-\"a28aadc\" (MC: 1.18.2)")

        assertEquals(MCVersion.MC_1_18_2, MCVersion.currentVersion)
    }
}
