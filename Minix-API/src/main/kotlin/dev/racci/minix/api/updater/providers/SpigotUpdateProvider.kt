package dev.racci.minix.api.updater.providers

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import dev.racci.minix.api.updater.ChecksumType
import dev.racci.minix.api.updater.UpdateResult
import dev.racci.minix.api.updater.Version
import dev.racci.minix.api.updater.providers.UpdateProvider.UpdateProviderSerializer.Companion.getBuffered
import io.ktor.client.statement.discardRemaining
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

/**
 * This update provider allows to query spigotmc.org for plugin updates.
 * Only plugins hosted on spigotmc.org can be downloaded automatically. Plugins hosted externally, that are only listed on spigotmc.org will only provide metadata.
 *
 * Creates an update provider for spigotmc.org.
 * Spigot doesn't provide a fileName. It will be assumed that the file is a .jar file
 *
 */
// TODO: Add support for noticing redirects to another valid update provider type
class SpigotUpdateProvider(
    internal val projectID: Int,
    internal val fileName: String = "$projectID.jar"
) : BaseOnlineProvider() {

    private var _lastResult: UpdateFile? = null
    private var downloadable = false
    private var lastResult get() = _lastResult
        set(value) { _lastResult = value }
    override val latestFileName get() = lastResult?.fileName
    override val latestFileURL: URL get() = lastResult?.downloadURL?.takeIf { downloadable }
        ?: throw RequestTypeNotAvailableException("The spigot update provider only allows to download resources hosted on spigotmc.org!")
    override val latestMinecraftVersion get() = lastResult?.gameVersion
    override val latestMinecraftVersions get() = lastResult?.gameVersions ?: emptyArray()
    override val latestName get() = lastResult?.name
    override val latestVersion get() = lastResult?.version
    override val name get() = "SpigotMC"
    override val providesChangelog get() = false
    override val providesChecksum get() = ChecksumType.NONE
    override val providesDependencies get() = false
    override val providesDownloadURL get() = downloadable
    override val providesMinecraftVersion get() = lastResult?.gameVersions != null
    override val providesMinecraftVersions get() = lastResult?.gameVersions != null
    override val providesUpdateHistory = false

    override suspend fun query(): UpdateResult = withContext(Dispatchers.IO) {
        val result = UpdateFile(fileName = fileName)
        val (response, latestResponse) = try {
            arrayOf(
                connect(URL("https://api.spiget.org/v2/resources/$projectID")) ?: return@withContext UpdateResult.FAILED_CONNECTION,
                connect(URL("https://api.spiget.org/v2/resources/$projectID/versions/latest")) ?: return@withContext UpdateResult.FAILED_CONNECTION
            )
        } catch (e: IOException) {
            logger.warn(e) { "Failed to connect to the spiget API!" }
            return@withContext UpdateResult.FAILED_CONNECTION
        }
        val jsonObj: JsonObject = response.getBuffered().use { JsonParser.parseReader(it).asJsonObject }

        result.name = jsonObj["name"].asString
        downloadable = !jsonObj["external"].asBoolean.also { bool ->
            if (bool) {
                result.downloadURL = URL("https://api.spiget.org/v2/resources/$projectID/download")
            } else result.downloadURL = URL("https://spigotmc.org/${jsonObj["file"].asJsonObject["url"].asString}")
        }

        jsonObj["testedVersions"].asJsonArray.takeUnless(JsonArray::isEmpty)?.apply {
            result.gameVersions = arrayOf(*map(JsonElement::getAsString).toTypedArray())
            result.gameVersion = last().asString
        }

        response.discardRemaining()

        val latestReader = latestResponse.getBuffered()
        val latestJsonObj = JsonParser.parseReader(latestReader).asJsonObject

        result.version = Version(latestJsonObj["name"].asString)
        latestResponse.discardRemaining()

        lastResult = result
        return@withContext UpdateResult.SUCCESS
    }
}
