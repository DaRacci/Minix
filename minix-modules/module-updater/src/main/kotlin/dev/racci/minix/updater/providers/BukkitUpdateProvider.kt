package dev.racci.minix.updater.providers

import com.google.gson.JsonParser
import dev.racci.minix.api.utils.Version
import dev.racci.minix.api.utils.data.Data.Companion.logger
import dev.racci.minix.updater.ChecksumType
import dev.racci.minix.updater.UpdateResult
import dev.racci.minix.updater.providers.UpdateProvider.UpdateProviderSerializer.Companion.getBuffered
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.MalformedURLException
import java.net.URL

public class BukkitUpdateProvider(
    internal val projectID: Int,
    internal val apiKey: String? = null
) : BaseOnlineProviderWithDownload() {

    private val url: URL = try {
        URL(HOST + projectID)
    } catch (e: Exception) {
        val throwable = InvalidUpdateProviderException(null, e)
        throw logger.fatal(throwable, "Error while creating URL for BukkitUpdateProvider!")
    }

    private var lastHistory: Array<UpdateFile>? = null

    public override val name: String get() = "dev.bukkit.org"

    public override suspend fun query(): UpdateResult = withContext(Dispatchers.IO) {
        val response = try {
            connect(url) ?: return@withContext UpdateResult.FAILED_NO_FILE
        } catch (e: IOException) {
            return@withContext if (e.message?.contains("HTTP response code: 403") == true) {
                logger.error(e) {
                    "dev.bukkit.org rejected the provided API key!" + "\n\t\tPlease check your API key and ensure it is correct."
                }
                UpdateResult.FAILED_KEY
            } else {
                logErrorOffline("dev.bukkit.org", e.message.orEmpty())
                UpdateResult.FAILED_NO_FILE
            }
        }

        val jsonObj = response.getBuffered().use { JsonParser.parseReader(it).asJsonArray }
        if (jsonObj.size() == 0) {
            logger.warn {
                "The updater couldn't find any files for the project with ID $projectID!" +
                    "\n\t\tPlease check your project ID and ensure it is correct."
            }
            return@withContext UpdateResult.FAILED_NO_FILE
        }

        try {
            lastHistory = jsonObj.mapIndexed { index, element ->
                val latestVersion = GSON.fromJson(element, BukkitJsonResponse::class.java)
                val url = URL(latestVersion.downloadUrl)
                val match = Version.versionStringRegex.find(latestVersion.name)
                when {
                    match != null -> {
                        lastResult = UpdateFile(
                            downloadURL = url,
                            name = latestVersion.name,
                            fileName = latestVersion.fileName,
                            checksum = latestVersion.md5,
                            gameVersion = latestVersion.gameVersion,
                            version = Version("${match.groupValues.joinToString("")}-${latestVersion.releaseType}")
                        )
                        lastResult!!
                    }

                    index == jsonObj.size() - 1 -> return@withContext UpdateResult.FAILED_NO_VERSION
                    else -> {
                        UpdateFile(
                            downloadURL = url,
                            name = latestVersion.name,
                            fileName = latestVersion.fileName,
                            checksum = latestVersion.md5,
                            gameVersion = latestVersion.gameVersion,
                            version = Version("0.0")
                        )
                    }
                }
            }.toTypedArray()
        } catch (e: MalformedURLException) {
            logger.warn(e) {
                "Failed to interpret the download url \"${lastHistory?.lastOrNull()?.downloadURL}\"!"
            }
            return@withContext UpdateResult.FAILED_NO_FILE
        }
        return@withContext UpdateResult.SUCCESS
    }

    override suspend fun requestBuilder(builder: HttpRequestBuilder) {
        if (apiKey != null) builder.headers.append("X-API-Key", apiKey)
    }

    public override val providesMinecraftVersion: Boolean get() = true
    public override val providesChecksum: ChecksumType get() = ChecksumType.MD5
    public override val providesUpdateHistory: Boolean get() = true
    public override val latestMinecraftVersion: String? get() = lastResult?.gameVersion // ?: throw NotSuccessfullyQueriedException()
    public override val latestChecksum: String? get() = lastResult?.checksum // ?: throw NotSuccessfullyQueriedException()
    public override val updateHistory: Array<UpdateFile>? get() = lastHistory // ?: throw NotSuccessfullyQueriedException()

    private data class BukkitJsonResponse(
        val dateReleased: String,
        val downloadUrl: String,
        val fileName: String,
        val fileUrl: String,
        val gameVersion: String,
        val md5: String,
        val name: String,
        val projectId: Long,
        val releaseType: String
    )

    public companion object {

        private const val HOST = "https://servermods.forgesvc.net/servermods/files?projectIds="
    }
}
