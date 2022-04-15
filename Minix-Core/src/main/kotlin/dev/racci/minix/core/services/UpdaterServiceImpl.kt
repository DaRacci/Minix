package dev.racci.minix.core.services

import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.data.PluginUpdater
import dev.racci.minix.api.data.UpdaterConfig
import dev.racci.minix.api.extensions.event
import dev.racci.minix.api.extensions.pm
import dev.racci.minix.api.extensions.server
import dev.racci.minix.api.extensions.taskAsync
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.services.DataService
import dev.racci.minix.api.services.UpdaterService
import dev.racci.minix.api.updater.ChecksumType
import dev.racci.minix.api.updater.UpdateMode
import dev.racci.minix.api.updater.UpdateResult
import dev.racci.minix.api.updater.providers.NotSuccessfullyQueriedException
import dev.racci.minix.api.updater.providers.RequestTypeNotAvailableException
import dev.racci.minix.api.updater.providers.UpdateProvider
import dev.racci.minix.api.utils.data.Data
import dev.racci.minix.api.utils.minecraft.MCVersion
import dev.racci.minix.api.utils.minecraft.MCVersion.Companion.sameMajor
import dev.racci.minix.api.utils.now
import dev.racci.minix.api.utils.size
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.discardRemaining
import io.ktor.utils.io.jvm.javaio.toInputStream
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
import java.math.RoundingMode
import java.security.DigestInputStream
import java.security.MessageDigest
import java.text.DecimalFormat
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream
import kotlin.time.Duration.Companion.minutes

@MappedExtension("Updater Service", [DataService::class], UpdaterService::class)
class UpdaterServiceImpl(override val plugin: Minix) : UpdaterService() {
    private val updaterConfig by lazy { get<DataService>().get<UpdaterConfig>() }
    private val updateFolder by lazy { // Ensure the update folder exists whenever we first get its location
        val folder = server.pluginsFolder.resolve(updaterConfig.updateFolder)
        if (!folder.mkdirs()) { log.error { "Could not create update folder!" } }
        folder
    }
    override val enabledUpdaters = mutableListOf<PluginUpdater>()
    override val disabledUpdaters = mutableListOf<PluginUpdater>()

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
                    "\n\t\tUpdaters:\n\t\t\t" + enabledUpdaters.joinToString(separator = "\n\t\t\t") { updater ->
                    "${updater.name} - ${updater.providers.joinToString(separator = ", ") { it.name }}"
                }
            }
        }

        taskAsync(repeatDelay = 5.minutes) {
            for (updater in enabledUpdaters) {
                log.debug { "Checking ${updater.name} for updates" }
                if (updater.lastRun != null && now() < (updater.lastRun!! + updaterConfig.interval)) continue

                if (updater.updateMode == UpdateMode.DISABLED) {
                    enabledUpdaters -= updater
                    disabledUpdaters += updater
                    continue
                }

                updater.provider.query()
                updater.lastRun = now()
                checkForUpdate(updater).takeIf { it && updater.updateMode == UpdateMode.UPDATE }?.let { update(updater) }
            }
        }
    }

    override suspend fun updateAll(): Array<Pair<String, UpdateResult>> = Array(enabledUpdaters.size) {
        val updater = enabledUpdaters[it]
        updater.name to tryUpdate(updater)
    }

    override fun checkAll(): Array<Pair<String, Boolean>> = Array(enabledUpdaters.size) {
        val updater = enabledUpdaters[it]
        updater.name to checkForUpdate(updater)
    }

    override suspend fun tryUpdate(updater: PluginUpdater): UpdateResult {
        updater.provider.query()
        var result = UpdateResult.NO_UPDATE
        checkForUpdate(updater).takeIf { it && updater.updateMode == UpdateMode.UPDATE }?.let { result = update(updater) }
        return result
    }

    override fun checkForUpdate(updater: PluginUpdater): Boolean {
        updater.result = when {
            updater.provider.latestVersion == null -> {
                plugin.log.warn {
                    "No remote version found for plugin ${updater.name}" +
                        "\n\t\tYou should contact the plugin author [${updater.pluginInstance?.description?.authors?.firstOrNull()}] about this!"
                }
                updater.failedAttempts++
                UpdateResult.FAILED_NO_VERSION
            }
            updater.localVersion >= updater.provider.latestVersion!! -> {
                if (updaterConfig.announceDownloadProgress && !updater.sentInfo) {
                    plugin.log.info {
                        "The plugin ${updater.name} is up to date." +
                            "\n\t\tLocal version: ${updater.localVersion}" +
                            "\n\t\tRemote version: ${updater.provider.latestVersion}"
                    }
                    updater.sentInfo = true // Ensure we only send this once instead of spamming the console every time we check.
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
        return updater.result!!.isSuccessful
    }

    private suspend fun update(updater: PluginUpdater): UpdateResult = withContext(Dispatchers.IO) {
        try {
            log.debug { "Updating ${updater.name} with ${updater.provider.name}" }
            val hashGenerator = updater.provider.providesChecksum.instanceOrNull
            val downloadFile = updateFolder.resolve(updater.provider.latestFileName ?: error("No latest file name found!"))
            updater.result = downloadFile(updater.provider, downloadFile, hashGenerator)

            hashCheck(hashGenerator, downloadFile, updater.provider).takeIf { it.name.startsWith("FAILED") }?.let { updater.result = it }
            val (file, _) = unzip(downloadFile).also { if (it.second.name.startsWith("FAILED")) updater.result = it.second }
            backupPlugin(updater).takeIf { it.name.startsWith("FAILED") }?.let { updater.result = it }
            readyPlugin(updater, file ?: downloadFile)

            if (updater.result?.name?.startsWith("FAILED") != true) {
                updater.result = UpdateResult.SUCCESS
                if (updaterConfig.announceDownloadProgress) log.info { "${updater.name} has been updated and readied for the next restart / reload." }
            }
        } catch (e: Exception) {
            updater.result = UpdateResult.FAILED_NO_FILE
            log.error(e) { "${updater.name} failed to update!" }
        }
        updater.result!!
    }

    private suspend fun downloadFile(
        provider: UpdateProvider,
        downloadFile: File,
        hashGenerator: MessageDigest?
    ): UpdateResult = withContext(Dispatchers.IO) {
        if (downloadFile.exists()) {
            log.debug { "File ${downloadFile.name} already exists, skipping download" }
            return@withContext UpdateResult.SUCCESS
        }

        val url = provider.latestFileURL ?: return@withContext UpdateResult.FAILED_NO_FILE
        var connection: HttpResponse? = null
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        return@withContext try {
            connection = provider.connect(url) ?: throw IOException("Could not connect to $url!")
            inputStream = hashGenerator?.let {
                DigestInputStream(connection.bodyAsChannel().toInputStream().buffered(), it)
            } ?: url.openStream().buffered()
            val fileLength = connection.bodyAsChannel().readLong()

            downloadFile.createNewFile()
            outputStream = downloadFile.outputStream()

            if (updaterConfig.announceDownloadProgress) { log.info { "Started downloading update: ${provider.latestVersion}" } }

            downloadWriter(inputStream, outputStream, fileLength)

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

    private fun downloadWriter(
        inputStream: InputStream,
        outputStream: OutputStream,
        fileLength: Long,
    ) {
        var count: Int
        var downloaded = 0
        var progress: Int
        var lastProgress = 1
        val percentPerByte = 100f / fileLength
        val buffer = ByteArray(BUFFER_SIZE)
        val size = getSize(fileLength)

        while (inputStream.read(buffer, 0, BUFFER_SIZE).also { count = it } != -1) {
            downloaded += count
            outputStream.write(buffer, 0, count)

            if (!updaterConfig.announceDownloadProgress || fileLength <= 0) continue

            progress = (downloaded * percentPerByte).toInt()
            if (progress % 10 == 0 && progress > lastProgress) {
                lastProgress = progress
                log.info { "Downloading update: $progress% ($size)" }
            }
        }
    }

    private fun getSize(
        fileLength: Long,
    ): String {
        if (!updaterConfig.announceDownloadProgress || fileLength <= 0) return ""
        return when {
            fileLength == 1L -> "1 byte"
            fileLength < 1024L -> "$fileLength bytes"
            else -> {
                var (doubleBytes, i) = byteType(fileLength)
                doubleBytes = roundByte(doubleBytes)

                "$doubleBytes ${BYTE_NAMES[i]}"
            }
        }
    }

    private fun byteType(length: Long): Pair<Double, Int> {
        var bytes = length.toDouble()
        var i = 1
        while (bytes >= 1024 && i < BYTE_NAMES.size) {
            bytes /= 1024.0
            i++
        }
        return bytes to i
    }

    private fun roundByte(bytes: Double) = DecimalFormat(if (bytes >= 100) "#.#" else "#.##").apply {
        roundingMode = RoundingMode.CEILING
    }.format(bytes).toDouble()

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
            val targetMD5 = updateProvider.latestChecksum?.lowercase()

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

                    val tempFile = extractEntry(zipFile, zipEntry)

                    if (!file.delete()) plugin.log.warn { "Couldn't delete old download file: ${file.absolutePath}" }
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

    private fun extractEntry(
        zipFile: ZipFile,
        zipEntry: ZipEntry
    ): File {
        val tempFile = File.createTempFile(plugin.name, null)
        val input = zipFile.getInputStream(zipEntry).buffered()

        tempFile.outputStream().buffered(BUFFER_SIZE).use(input::copyTo)

        input.close()
        zipFile.close()

        return tempFile
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

        val zipFile = updateFolder.resolve("${updater.name}_backup_${now().toLocalDateTime(TimeZone.UTC)}.zip")
        zipFile.createNewFile() // Shouldn't be a feasible way that this fails due to the name containing the current time
        val out = ZipOutputStream(zipFile.outputStream().buffered())

        fun traverseDir(directory: File) {
            directory.listFiles()?.forEach { file ->
                if (file.isDirectory) {
                    traverseDir(file)
                } else {
                    file.inputStream().buffered().use {
                        out.putNextEntry(ZipEntry(file.name))
                        BufferedInputStream(it).use { s -> s.copyTo(out, BUFFER_SIZE) }
                    }
                }
            }
        }

        traverseDir(folder)

        out.close()
        UpdateResult.SUCCESS
    }

    private fun isCompatible(updater: PluginUpdater): Boolean = try {
        val versions = updater.provider.latestMinecraftVersions?.map { MCVersion[it] }
        val supported = versions?.any { MCVersion.currentVersion sameMajor it } ?: false
        if (!supported) {
            log.info {
                "Update found but it isn't compatible with the current Minecraft version." +
                    "\n\t\tCurrent version: ${MCVersion.currentVersion.majorMinecraftVersion}" +
                    "\n\t\tSupported versions: ${versions?.joinToString(", ")}"
            }
        }
        supported
    } catch (e: Exception) {
        plugin.log.warn(e) { "The auto-updater was unable to check the compatibility of the new version." }
        updater.result = UpdateResult.FAILED_VERSION_INCOMPATIBLE
        false
    }

    private val newVersionBlock = { updater: PluginUpdater ->
        if (updaterConfig.announceDownloadProgress && !updater.sentAvailable) {
            plugin.log.info {
                "The plugin ${updater.name} has a new version." +
                    "\n\t\tLocal version: ${updater.localVersion}" +
                    "\n\t\tRemote version: ${updater.provider.latestVersion}"
            }
            updater.sentAvailable = true
        }
    }

    companion object {
        private const val BUFFER_SIZE = 1024
        private val BYTE_NAMES by lazy { arrayOf("byte", "bytes", "kiB", "MiB", "GiB", "TiB", "PiB", "EiB") }
    }
}
