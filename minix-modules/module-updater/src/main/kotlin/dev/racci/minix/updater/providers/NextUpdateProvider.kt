package dev.racci.minix.updater.providers

import dev.racci.minix.api.utils.Version
import dev.racci.minix.api.utils.now
import dev.racci.minix.updater.ChecksumType
import dev.racci.minix.updater.UpdateData
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.sync.Mutex
import kotlinx.datetime.Instant
import java.util.jar.JarFile
import kotlin.time.Duration.Companion.minutes

public sealed class NextUpdateProvider(
    public val name: String,
    public val localVersion: Version,
    internal val userAgent: String = "Minix Auto-Updater"
) {
    private lateinit var remoteVersion: Version
    private val lastCheck: Instant = Instant.DISTANT_PAST
    private val mutex = Mutex()

    public open val providesDownload: Boolean get() = false

    public open val providesSupportedVersions: Boolean get() = false

    public open val providesChecksum: ChecksumType get() = ChecksumType.NONE

    public open suspend fun requestBuilder(builder: HttpRequestBuilder) = Unit

    public open suspend fun processResponse(response: HttpResponse) = Unit

    public fun getLatestVersion(): Deferred<Version> {
        return if (!::remoteVersion.isInitialized || lastCheck + 30.minutes < now()) {
            this.asyncCheckUpdates()
        } else CompletableDeferred(remoteVersion)
    }

    public abstract fun asyncDownload(): Deferred<JarFile>

    public abstract fun asyncGetUpdateData(): Deferred<UpdateData>

    /**
     * Checks if there are any available updates.
     * * @return An Optional with the throwable if there was an error.
     */
    public fun asyncCheckUpdates(): Deferred<Nothing> {
        return CompletableDeferred()
    }

    public fun asyncUpdate() {
    }

    protected fun extractZip() {
    }

    public companion object {
        public const val CHECK_DELAY: Int = 30
    }
}
