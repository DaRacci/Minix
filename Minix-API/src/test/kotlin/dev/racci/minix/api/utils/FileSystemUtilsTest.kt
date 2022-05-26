package dev.racci.minix.api.utils

import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class FileSystemUtilsTest {

    private lateinit var file: File

    @BeforeEach
    fun setup() {
        file = mockk()
    }

    @AfterEach
    fun shutdown() {
        unmockkAll()
    }

    @Test
    fun `single file size is correct`() {
        every { file.isFile } returns true
        every { file.isDirectory } returns false
        every { file.length() } returns 512L

        assertEquals(512L, FileSystemUtils.size(file))

        verify { file.isFile }
        verify { file.length() }
    }

    @Test
    fun `depth 1 directory size is correct`() {
        every { file.isFile } returns false
        every { file.isDirectory } returns true
        val innerFile = mockk<File> {
            every { length() } returns 512L
            every { isDirectory } returns false
        }
        every { file.listFiles() } answers {
            val array = arrayOfNulls<File>(5)
            repeat(5) { array[it] = innerFile }
            array
        }

        assertEquals(512L * 5, FileSystemUtils.size(file))

        verify { file.isFile }
        verify { file.listFiles() }
        verify { innerFile.isDirectory }
        verify { innerFile.length() }
    }

    @Test
    fun `with a depth of 5 and mix of folders and files`() {
        every { file.isFile } returns false
        every { file.isDirectory } returns true
        val innerFile = mockk<File> {
            every { length() } returns 512L
            every { isDirectory } returns false
        }

        val innerDirectory = mockk<File>()
        every { innerDirectory.isDirectory } returns true
        every { innerDirectory.listFiles() } returns arrayOf(innerFile, innerDirectory, innerFile)

        every { file.listFiles() } answers {
            val array = arrayOfNulls<File>(5)
            repeat(5) { array[it] = innerDirectory }
            array
        }

        assertEquals(17920L, FileSystemUtils.size(file, 5))
    }
}
