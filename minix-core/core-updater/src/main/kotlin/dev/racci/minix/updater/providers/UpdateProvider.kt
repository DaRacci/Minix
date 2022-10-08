package dev.racci.minix.updater.providers

import dev.racci.minix.api.updater.ChecksumType
import dev.racci.minix.api.updater.Version
import dev.racci.minix.api.utils.now
import dev.racci.minix.updater.UpdateData
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.sync.Mutex
import kotlinx.datetime.Instant
import java.util.jar.JarFile
import kotlin.time.Duration.Companion.minutes

sealed class UpdateProvider(
    val name: String,
    val localVersion: Version,
    internal val userAgent: String = "Minix Auto-Updater"
) {
    private lateinit var remoteVersion: Version
    private val lastCheck: Instant = Instant.DISTANT_PAST
    private val mutex = Mutex()

    open val providesDownload: Boolean get() = false

    open val providesSupportedVersions: Boolean get() = false

    open val providesChecksum: ChecksumType get() = ChecksumType.NONE

    open suspend fun requestBuilder(builder: HttpRequestBuilder) = Unit

    open suspend fun processResponse(response: HttpResponse) = Unit

    fun getLatestVersion(): Deferred<Version> {
        return if (!::remoteVersion.isInitialized || lastCheck + 30.minutes < now()) {
            this.asyncCheckUpdates()
        } else CompletableDeferred(remoteVersion)
    }

    abstract fun asyncDownload(): Deferred<JarFile>

    abstract fun asyncGetUpdateData(): Deferred<UpdateData>

    /**
     * Checks if there are any available updates.
     * * @return An Optional with the throwable if there was an error.
     */
    fun asyncCheckUpdates(): Deferred<Nothing> {
        return CompletableDeferred()
    }

    fun asyncUpdate() {
    }

    protected fun extractZip() {
    }

    companion object {
        const val CHECK_DELAY = 30
    }
}
