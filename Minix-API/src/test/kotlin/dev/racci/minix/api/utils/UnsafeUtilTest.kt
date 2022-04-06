package dev.racci.minix.api.utils

import dev.racci.minix.api.updater.Version
import dev.racci.minix.api.utils.UnsafeUtil.Companion.unsafe
import dev.racci.minix.api.utils.kotlin.invokeIfNotNull
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

    @Test
    fun test() {
        var rawVersion = "2.6.3-SNAPSHOT"

        val matcher = Version.versionStringRegex.matchEntire(rawVersion) ?: throw Version.InvalidVersionStringException()
        val version = matcher.groups["version"]!!.value.replace(Version.unimportantVersionRegex, "")
        val comps = version.split(".").toTypedArray()
        val tags = matcher.groups["tags"]!!.value.split("-").toTypedArray()
        val tagsList = Version.getAll(tags, Version.preReleaseTags)
        val finalVersion = tagsList.isNullOrEmpty()

        println(comps.joinToString(", "))
        println(version.removeSuffix("-${tags.joinToString("-")}").split(".").toTypedArray().joinToString(", "))

        rawVersion = rawVersion.takeUnless { it.startsWith("v", true) } ?: rawVersion.substring(1)
        val VERSION = IntArray(comps.size.takeUnless { finalVersion } ?: (comps.size + 1))
        val TAGS = tags
        var isPreRelease = false

        println(comps.indices.joinToString(", "))

        for (i in comps.indices) { println("Taking ${comps[i]} for $i"); VERSION[i] = comps[i].toInt() }
        println(VERSION.joinToString("."))
        if (!finalVersion) {
            isPreRelease = true
            var last = 0
            for (string in tagsList) {
                if (last == 0) last = Int.MAX_VALUE
                var tagNumber = 0
                var tag = string.lowercase()
                Version.preReleaseTagRegex.matchEntire(tag).invokeIfNotNull { result ->
                    tagNumber = result.groups["number"]!!.value.toInt()
                    tag = result.groups["tag"]!!.value
                }
                last = last - Version.preReleaseTagResolution[tag]!! + tagNumber
            }
            VERSION[VERSION.lastIndex] = last
            if (last > 0) {
                for (i in VERSION.size - 2 downTo 0) {
                    if (VERSION[i] > 0 || i == 0) {
                        VERSION[i]--
                        break
                    }
                }
            }
        }

        println("Version: " + VERSION.joinToString(", "))
        println("TagsList: " + tagsList.joinToString(", "))
        println("Tags: " + TAGS.joinToString(", "))
        println("PreRelease: $isPreRelease")
    }
}
