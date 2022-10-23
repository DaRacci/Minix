package dev.racci.minix.updater.providers

import dev.racci.minix.api.utils.Version
import dev.racci.minix.updater.ChecksumType
import dev.racci.minix.updater.ReleaseType
import dev.racci.minix.updater.UpdateResult
import java.net.URL

public class NullUpdateProvider : UpdateProvider() {

    override val latestFileName: String get() = "null.jar"
    override val latestReleaseType: ReleaseType get() = ReleaseType.UNKNOWN
    override val latestVersion: Version get() = Version(Int.MAX_VALUE)
    override val name: String = "Null"
    override val providesChangelog: Boolean get() = false
    override val providesChecksum: ChecksumType get() = ChecksumType.NONE
    override val providesDependencies: Boolean get() = false
    override val providesDownloadURL: Boolean get() = false
    override val providesMinecraftVersion: Boolean get() = false
    override val providesMinecraftVersions: Boolean get() = false
    override val providesUpdateHistory: Boolean get() = false
    override suspend fun connect(url: URL): Nothing? = null
    override suspend fun query(): UpdateResult = UpdateResult.NO_UPDATE
}
