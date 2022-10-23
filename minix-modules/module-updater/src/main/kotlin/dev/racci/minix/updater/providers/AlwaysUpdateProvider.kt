package dev.racci.minix.updater.providers

import dev.racci.minix.updater.ReleaseType
import dev.racci.minix.updater.UpdateResult
import java.net.MalformedURLException
import java.net.URL

/**
 * An update provider that returns a very high version so that the file gets downloaded every time.
 * The file to download is specified in the constructor.
 * This provider should only be used for preview builds that will always download the newest build from a server.
 */
public class AlwaysUpdateProvider(
    public override val latestFileURL: URL,
    public override val latestFileName: String = "file.jar",
    override val latestReleaseType: ReleaseType = ReleaseType.RELEASE
) : BaseOnlineProvider() {

    public override val name: String = "Static URL"
    public override val latestVersion: Version get() = Version(Int.MAX_VALUE.toString() + "." + Int.MAX_VALUE)
    override val providesDownloadURL: Boolean get() = true

    public override suspend fun query(): UpdateResult = UpdateResult.SUCCESS

    /**
     * @throws UpdateProvider.InvalidUpdateProviderException if the URL is malformed
     */
    @Throws(UpdateProvider.InvalidUpdateProviderException::class)
    public constructor(
        url: String,
        latestFileName: String = "file.jar",
        releaseType: ReleaseType = ReleaseType.RELEASE
    ) : this(
        try {
            URL(url)
        } catch (e: MalformedURLException) {
            val throwable = UpdateProvider.InvalidUpdateProviderException("The given url is not a valid URL: $url")
            throwable.addSuppressed(e)
            throw throwable
        },
        latestFileName,
        releaseType
    )
}
