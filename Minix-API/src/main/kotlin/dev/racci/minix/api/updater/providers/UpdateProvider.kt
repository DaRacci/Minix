package dev.racci.minix.api.updater.providers

import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.updater.ChecksumType
import dev.racci.minix.api.updater.ReleaseType
import dev.racci.minix.api.updater.UpdateResult
import dev.racci.minix.api.updater.Version
import dev.racci.minix.api.utils.getKoin
import io.ktor.client.statement.HttpResponse
import java.io.IOException
import java.net.URL

abstract class UpdateProvider {

    val logger by lazy { getKoin().get<Minix>().log }

    /**
     * @return The name of the update provider.
     */
    abstract val name: String

    /**
     * Gets the latest version's version (such as 1.32)
     *
     * @return The latest version.
     * @throws NotSuccessfullyQueriedException  If the provider has not been queried successfully before
     */
    @get:Throws(NotSuccessfullyQueriedException::class)
    abstract val latestVersion: Version

    /**
     * Get the latest version's direct download url.
     *
     * @return latest version's file download url.
     * @throws RequestTypeNotAvailableException If the provider doesn't support the request type
     * @throws NotSuccessfullyQueriedException  If the provider has not been queried successfully before
     */
    @get:Throws(RequestTypeNotAvailableException::class, NotSuccessfullyQueriedException::class)
    open val latestFileURL: URL get() = throw RequestTypeNotAvailableException("The $name update provider does not provide a file url!")

    /**
     * Get the latest version's file name.
     *
     * @return latest version's file name.
     * @throws NotSuccessfullyQueriedException  If the provider has not been queried successfully before
     */
    @get:Throws(NotSuccessfullyQueriedException::class)
    abstract val latestFileName: String

    /**
     * Get the latest version's name (such as "Project v1.0").
     *
     * @return latest version's name.
     * @throws RequestTypeNotAvailableException If the provider doesn't support the request type
     * @throws NotSuccessfullyQueriedException  If the provider has not been queried successfully before
     */
    @get:Throws(RequestTypeNotAvailableException::class, NotSuccessfullyQueriedException::class)
    open val latestName: String get() = throw RequestTypeNotAvailableException("The $name update provider does not provide a name!")

    /**
     * Get the latest version's game version (such as "CB 1.7.2-R0.3" or "1.9").
     *
     * @return latest version's game version.
     * @throws RequestTypeNotAvailableException If the provider doesn't support the request type
     * @throws NotSuccessfullyQueriedException  If the provider has not been queried successfully before
     */
    @get:Throws(RequestTypeNotAvailableException::class, NotSuccessfullyQueriedException::class)
    open val latestMinecraftVersion: String get() = throw RequestTypeNotAvailableException("The $name update provider does not provide a minecraft version!")

    /**
     * Get the latest version's compatible game versions as an string array (each element is a compatible version such as "CB 1.7.2-R0.3" or "1.9").
     *
     * @return latest version's game version.
     * @throws RequestTypeNotAvailableException If the provider doesn't support the request type
     * @throws NotSuccessfullyQueriedException  If the provider has not been queried successfully before
     */
    @get:Throws(RequestTypeNotAvailableException::class, NotSuccessfullyQueriedException::class)
    open val latestMinecraftVersions: Array<String> get() = throw RequestTypeNotAvailableException("The $name update provider does not provide any minecraft versions!")

    /**
     * Get the latest version's release type.
     *
     * @return latest version's release type.
     *
     * @throws NotSuccessfullyQueriedException  If the provider has not been queried successfully before
     */
    @get:Throws(NotSuccessfullyQueriedException::class)
    abstract val latestReleaseType: ReleaseType

    /**
     * Get the latest version's checksum (md5).
     *
     * @return latest version's MD5 checksum.
     * @throws RequestTypeNotAvailableException If the provider doesn't support the request type
     * @throws NotSuccessfullyQueriedException  If the provider has not been queried successfully before
     */
    @get:Throws(RequestTypeNotAvailableException::class, NotSuccessfullyQueriedException::class)
    open val latestChecksum: String get() = throw RequestTypeNotAvailableException("The $name update provider does not provide a checksum!")

    /**
     * Get the latest version's changelog.
     *
     * @return latest version's changelog.
     * @throws RequestTypeNotAvailableException If the provider doesn't support the request type
     * @throws NotSuccessfullyQueriedException  If the provider has not been queried successfully before
     */
    @get:Throws(RequestTypeNotAvailableException::class, NotSuccessfullyQueriedException::class)
    open val latestChangelog: String get() = throw RequestTypeNotAvailableException("The $name update provider does not provide a changelog!")

    /**
     * Get the latest version's dependencies.
     *
     * @return latest version's dependencies. Empty collection if there are no dependencies.
     * @throws RequestTypeNotAvailableException If the provider doesn't support the request type
     * @throws NotSuccessfullyQueriedException  If the provider has not been queried successfully before
     */
    @get:Throws(RequestTypeNotAvailableException::class, NotSuccessfullyQueriedException::class)
    open val latestDependencies: Array<UpdateFile> get() = throw RequestTypeNotAvailableException("The $name update provider does not provide any dependencies!")

    /**
     * Get a collection of the latest versions.
     *
     * @return Array with the updates.
     * @throws RequestTypeNotAvailableException If the provider doesn't support the request type
     * @throws NotSuccessfullyQueriedException  If the provider has not been queried successfully before
     */
    @get:Throws(RequestTypeNotAvailableException::class, NotSuccessfullyQueriedException::class)
    open val updateHistory: Array<UpdateFile> get() = throw RequestTypeNotAvailableException("The $name update provider does not provide an update history!")

    open val providesDownloadURL: Boolean get() = false
    open val providesMinecraftVersion: Boolean get() = false
    open val providesMinecraftVersions: Boolean get() = false
    open val providesChangelog: Boolean get() = false
    open val providesChecksum: ChecksumType get() = ChecksumType.NONE
    open val providesUpdateHistory: Boolean get() = false
    open val providesDependencies: Boolean get() = false

    /**
     * Make a connection to the provider an requests the file's details.
     *
     * @return The update result from the query.
     */
    abstract suspend fun query(): UpdateResult

    /**
     * Opens a connection with all the required connection properties (like API keys)
     * @param url The url to connect to
     * @return The established connection. Null if a redirect loop was detected.
     * @throws IOException The exception caused while connecting.
     */
    @Throws(IOException::class)
    abstract suspend fun connect(url: URL): HttpResponse?

    class InvalidUpdateProviderException(message: String) : Exception(message)

    data class UpdateFile(
        var downloadURL: URL? = null,
        var name: String? = null,
        var fileName: String? = null,
        var checksum: String? = null,
        var changelog: String = "",
        var gameVersion: String? = null,
        var version: Version? = null,
        var gameVersions: Array<String>? = null,
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as UpdateFile

            if (downloadURL != other.downloadURL) return false
            if (name != other.name) return false
            if (fileName != other.fileName) return false
            if (checksum != other.checksum) return false
            if (changelog != other.changelog) return false
            if (gameVersion != other.gameVersion) return false
            if (version != other.version) return false
            if (gameVersions != null) {
                if (other.gameVersions == null) return false
                if (!gameVersions.contentEquals(other.gameVersions)) return false
            } else if (other.gameVersions != null) return false

            return true
        }

        override fun hashCode(): Int {
            var result = downloadURL?.hashCode() ?: 0
            result = 31 * result + (name?.hashCode() ?: 0)
            result = 31 * result + (fileName?.hashCode() ?: 0)
            result = 31 * result + (checksum?.hashCode() ?: 0)
            result = 31 * result + changelog.hashCode()
            result = 31 * result + (gameVersion?.hashCode() ?: 0)
            result = 31 * result + (version?.hashCode() ?: 0)
            result = 31 * result + (gameVersions?.contentHashCode() ?: 0)
            return result
        }

        override fun toString(): String {
            return "UpdateFile(downloadURL=$downloadURL, name=$name, fileName=$fileName, checksum=$checksum, changelog='$changelog', gameVersion=$gameVersion, version=$version, gameVersions=$gameVersions)"
        }
    }
}
