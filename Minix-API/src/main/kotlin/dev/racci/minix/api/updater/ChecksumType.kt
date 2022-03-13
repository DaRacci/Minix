package dev.racci.minix.api.updater

import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.utils.getKoin
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

enum class ChecksumType(
    /**
     * The algorithm for the [java.security.MessageDigest].
     */
    val algorithm: String? = null,
    /**
     * @return true if the checksum type is supported.
     */
    var isSupported: Boolean = false,
) {
    NONE(),
    MD5("MD5"),
    SHA1("SHA1"),
    SHA224("SHA-224"),
    SHA256("SHA-256"),
    SHA384("SHA-384"),
    SHA512("SHA-512");

    init {
        if (algorithm != null) {
            isSupported = try {
                MessageDigest.getInstance(algorithm)
                true
            } catch (e: NoSuchAlgorithmException) {
                getKoin().get<Minix>().log.error { "Hashing algorithm $algorithm is available on your system." }
                false
            }
        }
    }

    /**
     * Gets an instance of the [java.security.MessageDigest] hash provider.
     *
     * @return The instance of the hash provider.
     * @throws NoSuchAlgorithmException If the algorithm is not available on the system.
     */
    @get:Throws(NoSuchAlgorithmException::class)
    val instance: MessageDigest
        get() = MessageDigest.getInstance(algorithm)

    /**
     * Gets an instance of the [java.security.MessageDigest] hash provider.
     *
     * @return The instance of the hash provider. Null if the algorithm is not available on the system.
     */
    val instanceOrNull: MessageDigest?
        get() {
            if (!isSupported) return null
            try {
                return instance
            } catch (ignored: NoSuchAlgorithmException) {
            }
            return null
        }
}
