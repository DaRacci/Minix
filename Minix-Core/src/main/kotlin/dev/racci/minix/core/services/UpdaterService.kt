package dev.racci.minix.core.services

import dev.racci.minix.api.data.PluginUpdater
import dev.racci.minix.api.data.UpdaterConfig
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.event
import dev.racci.minix.api.extensions.pm
import dev.racci.minix.api.extensions.server
import dev.racci.minix.api.extensions.taskAsync
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.services.DataService
import dev.racci.minix.api.updater.ChecksumType
import dev.racci.minix.api.updater.UpdateMode
import dev.racci.minix.api.updater.UpdateResult
import dev.racci.minix.api.updater.providers.NotSuccessfullyQueriedException
import dev.racci.minix.api.updater.providers.RequestTypeNotAvailableException
import dev.racci.minix.api.updater.providers.UpdateProvider
import dev.racci.minix.api.utils.data.Data
import dev.racci.minix.api.utils.kotlin.ifFalse
import dev.racci.minix.api.utils.minecraft.MCVersion
import dev.racci.minix.api.utils.minecraft.MCVersion.Companion.sameMajor
import dev.racci.minix.api.utils.now
import dev.racci.minix.api.utils.size
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.discardRemaining
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.event.server.PluginEnableEvent
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.get
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.security.DigestInputStream
import java.security.MessageDigest
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream
import kotlin.time.Duration.Companion.minutes

class UpdaterService(override val plugin: Minix) : Extension<Minix>() {
    private val updaterConfig by lazy { get<DataService>().get<UpdaterConfig>() }
    private val updateFolder by lazy { server.pluginsFolder.resolve(updaterConfig.updateFolder) }
    private val enabledUpdaters = mutableListOf<PluginUpdater>()
    private val disabledUpdaters = mutableListOf<PluginUpdater>()

    override val name = "Updater Service"
    override val dependencies = persistentListOf(DataService::class)

    override suspend fun handleEnable() {
        event<PluginEnableEvent> {
            disabledUpdaters.firstOrNull { it.name == plugin.name }?.let {
                disabledUpdaters -= it
                enabledUpdaters += it
            }
        }
        event<PluginDisableEvent> {
            enabledUpdaters.firstOrNull { it.name == plugin.name }?.let {
                enabledUpdaters -= it
                disabledUpdaters += it
            }
        }

        if (!updaterConfig.enabled || updaterConfig.pluginUpdaters.isEmpty()) return

        for (updater in updaterConfig.pluginUpdaters) {
            updater.pluginInstance = pm.getPlugin(updater.name)

            when {
                updater.pluginInstance == null -> {
                    plugin.log.warn { "Couldn't find plugin with the name ${updater.name}" }
                    continue
                }
                updater.providers.isEmpty() -> {
                    plugin.log.warn { "Updater ${updater.name} has no providers" }
                    continue
                }
                else -> enabledUpdaters += updater
            }
        }

        if (enabledUpdaters.isNotEmpty()) { // Only debug if we found updater, but we always want to start the task
            log.debug {
                "Found ${enabledUpdaters.size} updaters" +
                    "\n\t\tUpdaters:" + enabledUpdaters.joinToString(separator = "\n\t\t\t") { updater ->
                    "${updater.name} - ${updater.providers.joinToString(separator = ", ") { it.second.name }}"
                }
            }
        }

        taskAsync(repeatDelay = 15.minutes) {
            for (updater in enabledUpdaters) {
                if (updater.lastRun == null || now() < (updater.lastRun!! + updater.interval)) continue

                if (updater.updateMode == UpdateMode.DISABLED) {
                    enabledUpdaters -= updater
                    disabledUpdaters += updater
                    continue
                }

                updater.provider.query()
                updater.lastRun = now()
                when (updater.updateMode) {
                    UpdateMode.UPDATE -> update(updater)
                    UpdateMode.CHECK -> versionCheck(updater)
                    else -> {}
                }
            }
        }
    }

    private suspend fun update(updater: PluginUpdater) = withContext(Dispatchers.IO) {
        if (!updateFolder.exists() && !updateFolder.mkdirs()) {
            return@withContext plugin.log.warn { "Could not create update folder!" }
        }

        try {
            val hashGenerator = updater.provider.providesChecksum.instanceOrNull
            val downloadFile = updateFolder.resolve(updater.provider.latestFileName)
            updater.result = downloadFile(updater.provider, downloadFile, hashGenerator)

            hashCheck(hashGenerator, downloadFile, updater.provider).takeIf { it.name.startsWith("FAILED") }?.let { updater.result = it }
            val (file, _) = unzip(downloadFile).also { if (it.second.name.startsWith("FAILED")) updater.result = it.second }
            backupPlugin(updater).takeIf { it.name.startsWith("FAILED") }?.let { updater.result = it }
            readyPlugin(updater, file!!)

            if (updater.result?.name?.startsWith("FAILED") != true) {
                updater.result = UpdateResult.SUCCESS
                if (updaterConfig.announceDownloadProgress) log.info { "${updater.name} has been updated and readied for the next restart / reload." }
            }
        } catch (e: Exception) {
            updater.result = UpdateResult.FAILED_NO_FILE
            log.error(e) { "${updater.name} failed to update!" }
        }
    }

    private suspend fun downloadFile(
        provider: UpdateProvider,
        downloadFile: File,
        hashGenerator: MessageDigest?
    ): UpdateResult = withContext(Dispatchers.IO) {
        val url = provider.latestFileURL
        var connection: HttpResponse? = null
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        return@withContext try {
            val tempConnection = provider.connect(url)
            if (tempConnection == null) {
                throw IOException("Could not connect to $url!")
            } else connection = tempConnection

            val fileLength = connection.bodyAsChannel().readLong()
            inputStream = if (hashGenerator != null) {
                DigestInputStream(connection.body<BufferedInputStream>(), hashGenerator)
            } else url.openStream().buffered()
            outputStream = downloadFile.outputStream()

            if (updaterConfig.announceDownloadProgress) { log.info { "Started downloading update: ${provider.latestVersion}" } }
            var count: Int
            var downloaded = 0
            var progress: Int
            var lastProgress = 1
            val percentPerByte = 100f / fileLength
            val buffer = ByteArray(BUFFER_SIZE)
            val size = if (updaterConfig.announceDownloadProgress && fileLength > 0) {
                when {
                    fileLength == 1L -> "1 byte"
                    fileLength < 1024L -> "$fileLength bytes"
                    else -> {
                        var doubleBytes = fileLength.toDouble()
                        var i = 1
                        while (doubleBytes >= 1024 && i < BYTE_NAMES.size) {
                            doubleBytes /= 1024.0
                            i++
                        }
                        String.format(if (doubleBytes >= 100) "%.1f %s" else "%.2f %s", doubleBytes, BYTE_NAMES[i])
                    }
                }
            } else ""

            while (inputStream.read(buffer, 0, BUFFER_SIZE).also { count = it } != -1) {
                downloaded += count
                outputStream.write(buffer, 0, count)

                if (updaterConfig.announceDownloadProgress && fileLength > 0) {
                    progress = (downloaded * percentPerByte).toInt()
                    if (progress % 10 == 0 && progress > lastProgress) {
                        lastProgress = progress
                        log.info { "Downloading update: $progress% ($size)" }
                    }
                }
            }
            UpdateResult.UPDATE_FOUND
        } catch (e: Exception) {
            return@withContext when (e) {
                is RequestTypeNotAvailableException -> {
                    log.error(e) { "The update provider supplied invalid capability data!" }
                    UpdateResult.FAILED_DOWNLOAD
                }
                is NotSuccessfullyQueriedException -> {
                    log.error(e) { "The update provider could not be queried!" }
                    UpdateResult.FAILED_CONNECTION
                }
                is IOException -> {
                    log.error(e) { "The auto-updater tried to download a new update but was unsuccessful." }
                    UpdateResult.FAILED_DOWNLOAD
                }
                else -> throw e
            }
        } finally {
            outputStream?.flush()
            outputStream?.close()
            inputStream?.close()
            connection?.discardRemaining()
        }
    }

    private suspend fun hashCheck(
        hashGenerator: MessageDigest?,
        downloadFile: File,
        updateProvider: UpdateProvider
    ): UpdateResult = withContext(Dispatchers.IO) {
        if (hashGenerator != null && updateProvider.providesChecksum == ChecksumType.MD5) {
            val digest = hashGenerator.digest()
            val downloadMD5 = if (digest.isNotEmpty()) {
                StringBuilder(digest.size * 2).apply {
                    for (b in digest) {
                        append(String.format("%02x", b))
                    }
                }.toString().lowercase()
            } else ""
            val targetMD5 = updateProvider.latestChecksum.lowercase()

            if (downloadMD5 != targetMD5) {
                plugin.log.warn {
                    "The auto-updater was able to download the file, however the MD5 checksum was incorrect! Deleting file." +
                        "\n\t\tMD5 checksum mismatch! Expected: $targetMD5, got: $downloadMD5"
                }
                if (downloadFile.delete()) {
                    plugin.log.info { "Deleted corrupted file." }
                } else { plugin.log.warn { "Could not delete corrupted file: ${downloadFile.absolutePath}" } }
                return@withContext UpdateResult.FAILED_CHECKSUM
            }
        }
        UpdateResult.SUCCESS
    }

    private suspend fun unzip(file: File): Pair<File?, UpdateResult> = withContext(Dispatchers.IO) {
        if (file.name.endsWith(".zip")) {
            // Search for a file matching this plugins name in the zip
            var newFile: File? = null
            return@withContext try {
                val zipFile = ZipFile(file)
                val zipEntries = zipFile.entries()
                val pattern = Regex("^(\\A${plugin.name})?.+\\.jar$")
                var gotFile = false
                while (zipEntries.hasMoreElements() && !gotFile) {
                    val zipEntry = zipEntries.nextElement()
                    if (zipEntry.isDirectory || !zipEntry.name.matches(pattern)) continue

                    val tempFile = File.createTempFile(plugin.name, null)
                    zipFile.getInputStream(zipEntry).buffered().use { inputStream ->
                        tempFile.outputStream().buffered(BUFFER_SIZE).use { outputStream ->
                            inputStream.copyTo(outputStream)
                            outputStream.flush()
                        }
                    }
                    zipFile.close()
                    file.delete().ifFalse { plugin.log.warn { "Couldn't delete old download file: ${file.absolutePath}" } }
                    newFile = file.parentFile.resolve(zipEntry.name)
                    tempFile.renameTo(newFile)
                    gotFile = true
                }
                if (!gotFile) {
                    plugin.log.warn {
                        "The auto-updater was able to download the zip file," +
                            "\n\t\thowever it did not contain file matching the plugin regex: ${pattern.pattern}" +
                            "\n\t\tThe zip file has been left in ${file.parentFile.absolutePath} for manual extraction."
                    }
                }
                newFile to UpdateResult.SUCCESS
            } catch (e: IOException) {
                plugin.log.warn(e) { "The auto-updater was unable to extract the downloaded zip file." }
                null to UpdateResult.FAILED_EXTRACTION
            }
        }
        null to UpdateResult.FAILED_EXTRACTION
    }

    private suspend fun readyPlugin(
        updater: PluginUpdater,
        file: File
    ): Unit = withContext(Dispatchers.IO) {
        try {
            transaction {
                DataServiceImpl.DataHolder.new(updater.pluginInstance!!.name) {
                    loadNext = file.toPath()
                }
            }
        } catch (e: Exception) {
            log.error(e) { "There was an issue getting ${updater.name} ready for update!" }
        }
    }

    private suspend fun backupPlugin(updater: PluginUpdater): UpdateResult = withContext(Dispatchers.IO) {
        val folder = updater.pluginInstance!!.dataFolder

        if (!folder.exists()) {
            plugin.log.warn { "The plugin data folder does not exist: ${folder.absolutePath}" }
            return@withContext UpdateResult.FAILED_BACKUP
        }

        val data = Data(folder.size())

        if (data > updaterConfig.backups.maxSize) {
            plugin.log.warn {
                "The plugin folder for ${updater.name} is too large to backup!" +
                    "\n\t\tCurrent size: ${data.humanReadableSize()}" +
                    "\n\t\tMax size: ${updaterConfig.backups.maxSize.humanReadableSize()}"
            }
            return@withContext UpdateResult.FAILED_BACKUP
        }

        val out = ZipOutputStream(updateFolder.resolve("${updater.name}_backup_${now().toLocalDateTime(TimeZone.UTC)}.zip").outputStream().buffered())
        val files: ArrayList<File> = ArrayList()
        folder.walk().onEnter { files += it; true }
        for (file in files) {
            file.inputStream().buffered().use {
                out.putNextEntry(ZipEntry(file.name))
                BufferedInputStream(it).use { s -> s.copyTo(out, BUFFER_SIZE) }
            }
        }
        out.close()
        UpdateResult.SUCCESS
    }

    private fun isCompatible(updater: PluginUpdater): Boolean = try {
        val versions = updater.provider.latestMinecraftVersions.map { MCVersion[it] }
        val supported = versions.any { MCVersion.currentVersion sameMajor it }
        if (!supported) {
            log.info {
                "Update found but it isn't compatible with the current Minecraft version." +
                    "\n\t\tCurrent version: ${MCVersion.currentVersion.majorMinecraftVersion}" +
                    "\n\t\tSupported versions: ${versions.joinToString(", ")}"
            }
        }
        supported
    } catch (e: Exception) {
        plugin.log.warn(e) { "The auto-updater was unable to check the compatibility of the new version." }
        updater.result = UpdateResult.FAILED_VERSION_INCOMPATIBLE
        false
    }

    // TODO: Implement more checks for compatibility
    private fun versionCheck(updater: PluginUpdater): Boolean {
        if (updater.remoteVersion == null) {
            plugin.log.warn {
                "No remote version found for plugin ${updater.name}" +
                    "\n\t\tYou should contact the plugin author [${updater.pluginInstance?.description?.authors?.firstOrNull()}] about this!"
            }
            updater.result = UpdateResult.FAILED_NO_VERSION
        }
        if (updater.localVersion >= updater.remoteVersion!!) {
            if (updaterConfig.announceDownloadProgress) {
                plugin.log.info {
                    "The plugin ${updater.name} is up to date." +
                        "\n\t\tLocal version: ${updater.localVersion}" +
                        "\n\t\tRemote version: ${updater.remoteVersion}"
                }
            }
            updater.result = UpdateResult.NO_UPDATE
        }
        if (updater.provider.providesMinecraftVersion) {
            updater.result = if (isCompatible(updater)) {
                if (updaterConfig.announceDownloadProgress) {
                    plugin.log.info {
                        "The plugin ${updater.name} is compatible with your Minecraft version." +
                            "\n\t\tLocal version: ${updater.localVersion}" +
                            "\n\t\tRemote version: ${updater.remoteVersion}"
                    }
                }
                UpdateResult.UPDATE_FOUND
            } else {
                UpdateResult.FAILED_VERSION_INCOMPATIBLE
            }
            updater.result = if (isCompatible(updater)) UpdateResult.UPDATE_FOUND else UpdateResult.FAILED_VERSION_INCOMPATIBLE
        }
        updater.result = when {
            updater.remoteVersion == null -> {
                plugin.log.warn {
                    "No remote version found for plugin ${updater.name}" +
                        "\n\t\tYou should contact the plugin author [${updater.pluginInstance?.description?.authors?.firstOrNull()}] about this!"
                }
                UpdateResult.FAILED_NO_VERSION
            }
            updater.localVersion >= updater.remoteVersion!! -> {
                if (updaterConfig.announceDownloadProgress) {
                    plugin.log.info {
                        "The plugin ${updater.name} is up to date." +
                            "\n\t\tLocal version: ${updater.localVersion}" +
                            "\n\t\tRemote version: ${updater.remoteVersion}"
                    }
                }
                UpdateResult.NO_UPDATE
            }
            updater.provider.providesMinecraftVersion -> {
                if (isCompatible(updater)) {
                    newVersionBlock(updater)
                    UpdateResult.UPDATE_FOUND
                } else {
                    UpdateResult.FAILED_VERSION_INCOMPATIBLE
                }
            }
            else -> {
                newVersionBlock(updater)
                UpdateResult.UPDATE_FOUND
            }
        }
        return updater.result?.isSuccessful == true
    }

    private val newVersionBlock = { updater: PluginUpdater ->
        if (updaterConfig.announceDownloadProgress) {
            plugin.log.info {
                "The plugin ${updater.name} has a new version." +
                    "\n\t\tLocal version: ${updater.localVersion}" +
                    "\n\t\tRemote version: ${updater.remoteVersion}"
            }
        }
    }

    companion object {
        private const val BUFFER_SIZE = 1024
        private val BYTE_NAMES by lazy { arrayOf("byte", "bytes", "kiB", "MiB", "GiB", "TiB", "PiB", "EiB") }
    }
}
