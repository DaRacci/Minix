package dev.racci.minix.api.updater.providers

import com.google.gson.Gson
import dev.racci.minix.api.updater.ReleaseType
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import java.net.URL

abstract class BaseOnlineProvider protected constructor(
    private val userAgent: String = "Minix Auto-Updater",
) : UpdateProvider() {

    override val latestReleaseType: ReleaseType
        get() {
            if (latestVersion == null) return ReleaseType.UNKNOWN
            val version = latestVersion!!
            if (!version.isPreRelease) return ReleaseType.RELEASE
            val sVersion = version.toString().lowercase()
            return when {
                sVersion.contains("alpha") || sVersion.contains("a") -> ReleaseType.ALPHA
                sVersion.contains("beta") || sVersion.contains("b") -> ReleaseType.BETA
                sVersion.contains("-rc") -> ReleaseType.RC
                sVersion.contains("snapshot") -> ReleaseType.SNAPSHOT
                else -> ReleaseType.UNKNOWN
            }
        }
    override val providesDownloadURL get() = true

    protected fun logErrorOffline(
        host: String,
        message: String
    ) = logger.error {
        "Failed to check for updates on $host" +
            "\n\t\tIf this has previously worked, the website may be temporarily down." +
            "\n\t\tMessage: $message"
    }

    override suspend fun connect(url: URL): HttpResponse? {
        var response: HttpResponse? = null
        var targetURL = url
        var redirects = 0
        while (response?.status != HttpStatusCode.OK) {
            if (++redirects >= 5) return null // 5 redirects is probably too many
            response = client.request {
                url(targetURL)
                headers.append(HttpHeaders.UserAgent, userAgent)
                requestBuilder(this)
            }
            when (response.status) {
                HttpStatusCode.TemporaryRedirect,
                HttpStatusCode.MovedPermanently,
                HttpStatusCode.SeeOther -> targetURL = URL(response.headers[HttpHeaders.Location])
                HttpStatusCode.OK -> continue
                else -> break // There was an issue, but this should be handled somewhere else.
            }
        }
        return response
    }

    protected open suspend fun requestBuilder(builder: HttpRequestBuilder) {}

    companion object {
        @JvmStatic
        protected val client: HttpClient = HttpClient(CIO) {
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

        @JvmStatic
        protected val GSON by lazy(::Gson)
        private const val PROPERTY_USER_AGENT = "User-Agent"
        private const val TIMEOUT = 5000
    }
}
