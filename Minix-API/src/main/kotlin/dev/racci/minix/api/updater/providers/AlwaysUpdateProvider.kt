package dev.racci.minix.api.updater.providers

import dev.racci.minix.api.updater.ReleaseType
import dev.racci.minix.api.updater.UpdateResult
import dev.racci.minix.api.updater.Version
import java.net.MalformedURLException
import java.net.URL

/**
 * An update provider that returns an very high version so that the file gets downloaded every time.
 * The file to download is specified in the constructor.
 * This provider should only be used for preview builds that will always download the newest build from a server.
 */
class AlwaysUpdateProvider(
    override val latestFileURL: URL,
    override val latestFileName: String = "file.jar",
    override val latestReleaseType: ReleaseType = ReleaseType.RELEASE
) : BaseOnlineProvider() {

    override val name = "Static URL"
    override val latestVersion get() = Version(Int.MAX_VALUE.toString() + "." + Int.MAX_VALUE)
    override val providesDownloadURL get() = true

    override suspend fun query() = UpdateResult.SUCCESS

    /**
     * @throws UpdateProvider.InvalidUpdateProviderException if the URL is malformed
     */
    @Throws(InvalidUpdateProviderException::class)
    constructor(
        url: String,
        latestFileName: String = "file.jar",
        releaseType: ReleaseType = ReleaseType.RELEASE
    ) : this(
        try {
            URL(url)
        } catch (e: MalformedURLException) {
            val throwable = InvalidUpdateProviderException("The given url is not a valid URL: $url")
            throwable.addSuppressed(e)
            throw throwable
        },
        latestFileName,
        releaseType
    )
}
