package dev.racci.minix.api.updater.providers

import dev.racci.minix.api.updater.ChecksumType
import dev.racci.minix.api.updater.ReleaseType
import dev.racci.minix.api.updater.UpdateResult
import dev.racci.minix.api.updater.Version
import org.spongepowered.configurate.ConfigurationNode
import java.lang.reflect.Type
import java.net.URL

class NullUpdateProvider : UpdateProvider() {

    override val latestFileName get() = "null.jar"
    override val latestReleaseType get() = ReleaseType.UNKNOWN
    override val latestVersion get() = Version(Int.MAX_VALUE)
    override val name = "Null"
    override val providesChangelog get() = false
    override val providesChecksum get() = ChecksumType.NONE
    override val providesDependencies get() = false
    override val providesDownloadURL get() = false
    override val providesMinecraftVersion get() = false
    override val providesMinecraftVersions get() = false
    override val providesUpdateHistory get() = false
    override suspend fun connect(url: URL): Nothing? = null
    override suspend fun query() = UpdateResult.NO_UPDATE
}
