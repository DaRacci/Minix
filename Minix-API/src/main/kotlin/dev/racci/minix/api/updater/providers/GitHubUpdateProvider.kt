package dev.racci.minix.api.updater.providers

import com.google.gson.JsonParser
import dev.racci.minix.api.updater.ChecksumType
import dev.racci.minix.api.updater.UpdateResult
import dev.racci.minix.api.updater.Version
import io.ktor.client.call.body
import io.ktor.client.statement.discardRemaining
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.intellij.lang.annotations.Language
import java.io.BufferedReader
import java.net.MalformedURLException
import java.net.URL

/**
 * This update provider allows to check github for new releases.
 */
class GitHubUpdateProvider(
    internal val projectOwner: String,
    internal val projectRepo: String,
    internal val userAgent: String = projectRepo,
    @Language("RegExp") internal val jarSearchRegex: String = ".*\\.jar$",
    @Language("RegExp") internal val md5SearchRegex: String = ".*\\.md5$",
) : BaseOnlineProviderWithDownload(userAgent) {

    private val url: URL
    private val assetJarPattern by lazy { Regex(jarSearchRegex, RegexOption.IGNORE_CASE) }
    private val assetMD5Pattern by lazy { Regex(md5SearchRegex, RegexOption.IGNORE_CASE) }

    // TODO add api key
    init {
        val builder = StringBuilder("https://api.github.com/repos/")
        builder.append(projectOwner)
        builder.append('/')
        builder.append(projectRepo)
        builder.append("/releases/latest")
        url = try {
            URL(builder.toString())
        } catch (e: MalformedURLException) {
            val throwable = InvalidUpdateProviderException(
                "Failed to build github api url!" +
                    "\n\t\tOwner: $projectOwner" +
                    "\n\t\tRepo: $projectRepo" +
                    "\n\t\tBuild URL: $builder"
            ).also { it.addSuppressed(e) }
            logger.throwing(throwable)
            throw throwable
        }
    }

    override val name: String = "GitHub"

    override suspend fun query(): UpdateResult = withContext(Dispatchers.IO) {
        val response = try {
            connect(url) ?: return@withContext UpdateResult.FAILED_NO_FILE
        } catch (e: IOException) {
            return@withContext if (e.message?.contains("HTTP response code: 403") == true) {
                logger.error(e) {
                    "api.github.com rejected the provided API key!" +
                        "\n\t\tPlease check your API key and ensure it is correct."
                }
                UpdateResult.FAILED_KEY
            } else {
                logErrorOffline("api.github.com", e.message.orEmpty())
                UpdateResult.FAILED_NO_FILE
            }
        }

        try {
            val result = UpdateFile(name = projectRepo)
            val jsonObj = response.body<BufferedReader>().use { JsonParser.parseReader(it).asJsonObject }
            val assets = jsonObj["assets"].asJsonArray
            var foundDL = false
            repeat(assets.size()) {
                val asset = assets[it].asJsonObject
                val name = asset["name"].asString
                val url = asset["browser_download_url"].asString
                when {
                    assetJarPattern.matches(name) -> {
                        result.fileName = name
                        result.downloadURL = URL(url)
                        foundDL = true
                    }
                    assetMD5Pattern.matches(name) -> result.checksum = getMD5FromUrl(url)
                }
            }
            if (!foundDL) return@withContext UpdateResult.FAILED_NO_FILE
            result.version = Version(jsonObj["tag_name"].asString)
            lastResult = result
        } catch (e: Exception) {
            logger.error(e) { "Failed to parse github response!" }
            return@withContext UpdateResult.FAILED_NO_VERSION
        } finally { response.discardRemaining() }
        return@withContext UpdateResult.SUCCESS
    }

    /**
     * Downloads a md5 file and searches for the md5 of the update
     *
     * @param url The url to the md5 file.
     * @return The md5 hash. Empty string if no hash was found.
     */
    private suspend fun getMD5FromUrl(url: String): String {
        try {
            val response = connect(URL(url))!!
            response.body<BufferedReader>().use { reader ->
                var line: String
                while (reader.readLine().also { line = it } != null) {
                    val match = IN_MD_5_SEARCH_PATTERN.find(line) ?: continue
                    if (assetJarPattern.matches(match.groups["file"]!!.value)) return match.groups["hash"]!!.value
                }
            }
        } catch (ignored: Exception) {}
        return ""
    }

    @get:Throws(RequestTypeNotAvailableException::class, NotSuccessfullyQueriedException::class)
    override val latestChecksum: String
        get() {
            if (lastResult == null || lastResult?.checksum.isNullOrEmpty()) throw NotSuccessfullyQueriedException()
            return lastResult!!.checksum!!
        }

    override val providesMinecraftVersions get() = false // TODO: I think this is possible, but I don't know how to do it.

    override val providesMinecraftVersion get() = false // TODO: I think this is possible, but I don't know how to do it.

    override val providesChangelog get() = false // TODO: Implement changelog

    override val providesChecksum: ChecksumType
        get() {
            if (lastResult == null ||
                !lastResult?.checksum.isNullOrEmpty()
            ) return ChecksumType.MD5
            return ChecksumType.NONE
        }

    override val providesUpdateHistory get() = false // TODO: Use github tags to get the update history

    override val providesDependencies get() = false // TODO: I don't know if this is possible but maybe it is?

    companion object {

        // TODO allow to chose checksum type
        private val IN_MD_5_SEARCH_PATTERN = Regex("(?<hash>[\\da-fA-F]{32})\\s+\\*?(?<file>.*)")
    }
}
