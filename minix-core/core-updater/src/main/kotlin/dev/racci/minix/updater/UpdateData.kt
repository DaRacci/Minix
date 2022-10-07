package dev.racci.minix.updater

import arrow.core.Invalid
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.Valid
import arrow.core.Validated
import co.aikar.timings.TimingsManager.url
import com.google.gson.Gson
import dev.racci.minix.api.plugin.logger.MinixLogger
import dev.racci.minix.api.updater.ChecksumType
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.api.utils.minecraft.MCVersion
import dev.racci.minix.updater.providers.UpdateProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.utils.EmptyContent.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import org.apache.logging.log4j.core.net.UrlConnectionFactory.createConnection
import sun.net.www.protocol.http.HttpURLConnection.userAgent
import java.io.IOException
import java.net.MalformedURLException
import java.net.URI
import java.net.URL
import java.util.jar.JarFile

open class UpdateData(
    private val provider: UpdateProvider,
    val supportedVersions: Collection<MCVersion>,
    val downloadURI: URI
) {
    val supportsCurrent: Boolean by lazy {
        if (!provider.providesSupportedVersions) return@lazy true

        if (supportedVersions.isEmpty()) {
            getKoin().get<MinixLogger>().warn { "${provider.name} doesn't list any supported versions, if you run into issues please manually downgrade this plugin." }
            return@lazy true
        }

        supportedVersions
            .filter { it.versionID == MCVersion.currentVersion.versionID }
            .onEach {
                if (it.protocolVersion == MCVersion.currentVersion.protocolVersion) return@onEach
                getKoin().get<MinixLogger>().warn { "The plugin supports the current version of Minecraft, but the protocol version is different. This may cause issues with the plugin." }
            }.isNotEmpty()
    }

    suspend fun getFile(): Deferred<Option<Throwable>> {
        if (!provider.providesDownload) return CompletableDeferred(Some(IllegalStateException("This provider doesn't provide a download link.")))
        if (!supportsCurrent) return CompletableDeferred(Some(IllegalStateException("The plugin doesn't support the current version of Minecraft.")))

        createConnection()

        return CompletableDeferred(None)
    }

    fun validateChecksum(): Deferred<Boolean> {
        if (provider.providesChecksum == ChecksumType.NONE) return CompletableDeferred(true)

        return CompletableDeferred(true)
    }

    fun getJarFile(): Deferred<JarFile> {
        return CompletableDeferred(JarFile(""))
    }

    @Throws(MalformedURLException::class)
    suspend fun createConnection(): Validated<Throwable, HttpResponse> {
        var response: HttpResponse
        var targetURL = downloadURI.toURL()
        var redirects = 0

        do {
            if (++redirects >= 5) return Invalid(IOException("Too many redirects."))

            response = runCatching {
                client.request {
                    url(targetURL)
                    headers.append(HttpHeaders.UserAgent, userAgent)
                    provider.requestBuilder(this)
                }
            }.fold(
                onSuccess = { it },
                onFailure = { return Invalid(it) }
            )

            when (response.status) {
                HttpStatusCode.TemporaryRedirect,
                HttpStatusCode.MovedPermanently,
                HttpStatusCode.SeeOther -> targetURL = URL(response.headers[HttpHeaders.Location])
                else -> break
            }
        } while (response.status != HttpStatusCode.OK)

        return Valid(response)
    }

    private fun unzipFile() {
    }

    private companion object {
        val client: HttpClient = HttpClient(CIO) {
            followRedirects = true
            engine {
                threadsCount = 1
                requestTimeout = 5000
                maxConnectionsCount = 100
                endpoint {
                    maxConnectionsCount = 10
                    pipelineMaxSize = 5
                    keepAliveTime = 5000
                    connectTimeout = 5000
                    connectAttempts = 5
                }
            }
        }

        val GSON by lazy(::Gson)
    }
}
