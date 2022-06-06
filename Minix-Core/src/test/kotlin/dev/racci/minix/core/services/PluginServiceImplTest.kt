package dev.racci.minix.core.services

import dev.racci.minix.api.extension.Extension
import io.mockk.mockk
import io.mockk.mockkClass
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.test.mock.MockProvider

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PluginServiceImplTest {

    @get:ExtendWith
    val mockProvider = MockProvider.register { clazz ->
        mockkClass(clazz)
    }

    @Test
    fun `returns all layered extensions`() {
        mockk<PluginServiceImpl>()
        mockk<Extension<*>>()
    }
}
