package dev.racci.minix.api.updater.providers

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import dev.racci.minix.api.updater.ChecksumType
import dev.racci.minix.api.updater.UpdateResult
import dev.racci.minix.api.updater.Version
import dev.racci.minix.api.updater.providers.UpdateProvider.UpdateProviderSerializer.Companion.getBuffered
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.intellij.lang.annotations.Language
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class JenkinsUpdateProvider @Throws(InvalidUpdateProviderException::class) constructor(
    internal val host: String,
    internal val job: String,
    internal val token: String? = null,
    @Language("RegExp") artifactSearchRegex: String? = null
) : BaseOnlineProviderWithDownload() {

    internal val artifactSearchRegex: Regex? = artifactSearchRegex?.let { Regex(it) }
    private val url: URL

    override val name = "Jenkins"
    override val latestChecksum get() = lastResult?.checksum // ?: throw NotSuccessfullyQueriedException()
    override val latestChangelog get() = lastResult?.changelog // ?: throw NotSuccessfullyQueriedException()
    override val providesChangelog get() = true
    override val providesChecksum get() = ChecksumType.MD5
    override val providesUpdateHistory get() = false // TODO

    override suspend fun query(): UpdateResult = withContext(Dispatchers.IO) {
        val result = UpdateFile()
        val response = getResponse().let { if (it is HttpResponse) it else return@withContext it as UpdateResult }

        try {
            val jsonObj = response.getBuffered().use(JsonParser::parseReader).asJsonObject

            setResult(jsonObj, result)
            setVersion(jsonObj, result)
            lastResult = result
        } catch (e: Exception) {
            logger.error(e) { "Failed to parse the result from the server" }
            return@withContext UpdateResult.FAILED_NO_VERSION
        }
        UpdateResult.SUCCESS
    }

    private suspend fun getResponse() = withContext(Dispatchers.IO) {
        return@withContext try {
            connect(url) ?: return@withContext UpdateResult.FAILED_CONNECTION
        } catch (e: Exception) {
            val code = e.message?.contains("HTTP response code: 403") ?: false
            when {
                code && token == null -> logger.error {
                    "The Jenkins server requires authentication, but no token was provided. " +
                        "Please add a token to your configuration."
                }
                code -> logger.error {
                    "The Jenkins server requires authentication, but the provided token is invalid. " +
                        "Please check your configuration."
                }
                else -> logErrorOffline(host, e.message.orEmpty())
            }
            return@withContext UpdateResult.FAILED_KEY
        }
    }

    private fun setResult(
        jsonObj: JsonObject,
        result: UpdateFile
    ) {
        val (artifactId, fileName, relativePath) = getArtifactId(jsonObj)

        result.fileName = fileName
        result.name = jsonObj["fullDisplayName"].asString
        result.downloadURL = URL("${jsonObj["url"].asJsonPrimitive.asString}artifact/$relativePath")
        val array = jsonObj["fingerprint"].asJsonArray
        result.checksum = when {
            !array.isEmpty -> array[artifactId]?.asJsonObject?.get("hash")?.asString
            else -> null
        }

        val items = jsonObj["changeSet"].asJsonObject["items"].asJsonArray
        result.changelog = StringBuilder().also { builder ->
            repeat(items.size()) {
                if (it != 0) builder.append("\n")
                builder.append(items[it].asJsonObject["comment"].asString)
            }
        }.toString()
    }

    private fun getArtifactId(jsonObj: JsonObject): Triple<Int, String, String> {
        var result: Triple<Int, String, String>? = null
        val artifacts = jsonObj["artifacts"].asJsonArray

        for ((index, element) in artifacts.withIndex()) {
            val artifact = element.asJsonObject
            val artifactName = artifact["fileName"].asString

            if (artifactSearchRegex == null || !artifactName.matches(artifactSearchRegex) ||
                artifactName.endsWith(".sources", true) ||
                artifactName.endsWith(".javadoc", true)
            ) continue

            result = Triple(index, artifactName, artifact["relativePath"].asString)
        }

        return result ?: Triple(-0, "", "")
    }

    private fun setVersion(
        jsonObj: JsonObject,
        result: UpdateFile
    ) {
        VERSION_PATTERN.matchEntire(result.fileName!!)?.let { matcher ->
            val versionBuilder = StringBuilder(matcher.groups["VersionString"]!!.value)
            versionBuilder.append("-T")
            SimpleDateFormat("yyyyMMddHHmmss")
                .also { it.timeZone = TimeZone.getTimeZone("UTC") }
                .format(Date(jsonObj["timestamp"].asJsonPrimitive.asLong))
                .let(versionBuilder::append)
            versionBuilder.append("-b")
            versionBuilder.append(jsonObj["number"].asJsonPrimitive.asString)
            val version = Version(versionBuilder.toString())
            result.version = version
        }
    }

    init {
        val builder: StringBuilder = StringBuilder()
        url = try {
            if (!host.contains("://")) builder.append("https://")
            builder.append(host)
            if (!host.endsWith("/")) builder.append('/')
            builder.append("job/${job.replace(" ", "%20")}")
            builder.append("/lastSuccessfulBuild/api/json?$API_FILTER")
            token?.let { builder.append("&token=$it") }
            logger.debug { "Built Jenkins URL - $builder" }
            URL(builder.toString())
        } catch (e: Exception) {
            val throwable = InvalidUpdateProviderException(null, e)
            throw logger.fatal(throwable) { "Failed to build jenkins api URL! [Host=$host | Job=$job | Token=$token | URL=$builder]" }
        }
    }

    companion object {

        private val VERSION_PATTERN by lazy { Regex(".*-(?<VersionString>${Version.versionStringRegex.pattern})\\.(jar|zip)") }
        private const val API_FILTER = "tree=artifacts[relativePath,fileName],fingerprint[hash],number,timestamp,url,fullDisplayName,changeSet[items[comment]]"
    }
}
