package dev.racci.minix.api.utils

import dev.racci.minix.api.utils.UnsafeUtil.Companion.unsafe
import strikt.api.expectCatching
import strikt.api.expectThrows
import kotlin.test.Test
import kotlin.test.assertSame
import kotlin.test.assertTrue

internal class UnsafeUtilTest {

    @Test
    fun danger() {
        var message = ""
        unsafe {
            danger {
                throw RuntimeException("Danger!")
            }
            catch(RuntimeException::class) {
                message = this.message!!
            }
        }
        assertSame(message, "Danger!")

        expectCatching {
            expectThrows<RuntimeException> {
                unsafe {
                    danger {
                        throw RuntimeException("Danger!")
                    }
                    catch(ClassCastException::class) {}
                }
            }
        }
    }

    @Test
    fun finally() {
        var bool = false
        unsafe {
            danger {
                throw RuntimeException("Danger!")
            }
            finally {
                bool = true
            }
        }
        assertTrue(bool)
    }
}
