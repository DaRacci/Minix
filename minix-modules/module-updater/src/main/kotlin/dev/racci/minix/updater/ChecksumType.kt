package dev.racci.minix.updater

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

public enum class ChecksumType(
    /** The algorithm for the [java.security.MessageDigest]. */
    public val algorithm: String? = null,

    /** If the checksum type is supported. */
    public var isSupported: Boolean = false
) {
    NONE,
    MD5("MD5"),
    SHA1("SHA1"),
    SHA224("SHA-224"),
    SHA256("SHA-256"),
    SHA384("SHA-384"),
    SHA512("SHA-512");

    init {
        if (algorithm != null) {
            isSupported = try {
                MessageDigest.getInstance(algorithm) != null
            } catch (e: NoSuchAlgorithmException) {
                error("Hashing algorithm $algorithm is available on your system.")
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
    public val instance: MessageDigest
        get() = MessageDigest.getInstance(algorithm)

    /**
     * Gets an instance of the [java.security.MessageDigest] hash provider.
     *
     * @return The instance of the hash provider. Null if the algorithm is not available on the system.
     */
    public val instanceOrNull: MessageDigest?
        get() {
            if (!isSupported) return null
            runCatching { return instance }
            return null
        }
}
