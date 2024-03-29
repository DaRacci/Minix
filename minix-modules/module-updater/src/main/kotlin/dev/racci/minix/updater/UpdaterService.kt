package dev.racci.minix.updater

import dev.racci.minix.api.extensions.taskAsync
import dev.racci.minix.api.lifecycles.ComplexManagedLifecycle
import dev.racci.minix.api.utils.data.Data
import dev.racci.minix.api.utils.data.Data.Companion.logger
import dev.racci.minix.api.utils.now
import dev.racci.minix.api.utils.size
import dev.racci.minix.updater.data.PluginUpdater
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.discardRemaining
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withTimeout
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.math.RoundingMode
import java.nio.file.Path
import java.security.DigestInputStream
import java.security.MessageDigest
import java.text.DecimalFormat
import java.util.Collections
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

// TODO: Refactor this class
public abstract class UpdaterService : ComplexManagedLifecycle {
    protected val updaters: MutableList<PluginUpdater> = Collections.synchronizedList(mutableListOf())

    abstract val updaterConfig: UpdaterConfig
    abstract val updateFolder: Path

//    private val updaterConfig by lazy { get<DataService>().get<UpdaterConfig>() }
//    private val updateFolder by lazy { // Ensure the update folder exists whenever we first get its location
//        val folder = plugin.dataFolder.resolve(updaterConfig.updateFolder)
//        if (!folder.exists() && !folder.mkdirs()) {
//            logger.error { "Could not create update folder!" }
//        }
//        folder
//    }
//    override val enabledUpdaters: MutableList<PluginUpdater> = Collections.synchronizedList(mutableListOf<PluginUpdater>())
//    override val disabledUpdaters: MutableList<PluginUpdater> = Collections.synchronizedList(mutableListOf<PluginUpdater>())

    protected suspend fun handleCreate() {
        val plugins = pm.plugins

        get<DataService>().withDatabase {
            for (holder in DataServiceImpl.DataHolder.all()) {
                val enabledPlugin = plugins.find { it.name == holder.id.value } ?: continue
                val currentName = enabledPlugin::class.java.protectionDomain.codeSource.location.file.substringAfterLast("/")

                logger.debug { "Found plugin ${enabledPlugin.name} with current file name $currentName" }

                when (currentName) {
                    holder.newVersion -> {
                        logger.info { "Plugin ${enabledPlugin.name} successfully updated to version ${holder.newVersion}" }

                        val oldFile = server.pluginsFolder.resolve(holder.oldVersion)
                        if (oldFile.exists()) {
                            logger.debug { "Moving old version of ${plugin.name}." }
                            Files.move(oldFile, updateFolder.resolve("old-versions/${holder.oldVersion}"))
                        }

                        holder.delete()
                    }
                    holder.oldVersion -> logger.warn {
                        """
                            Plugin ${plugin.name} is out of date!
                            Version ${plugin.description.version} has loaded instead of ${holder.newVersion.split("^${holder.id.value}-?".toRegex()).first()}.
                            Please manually update the plugin to the latest version.
                        """.trimIndent()
                    }
                    else -> holder.delete()
                }
            }
        }
    }

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

        val pluginService = get<PluginService>()
        pluginService.loadedPlugins.values.mapNotNull(MinixPlugin::updater).forEach { updater ->
            if (updaterConfig.pluginUpdaters.any { updater.name == it.name }) return@forEach
            updaterConfig.pluginUpdaters += updater
            enabledUpdaters += updater
        }

        updaterConfig.pluginUpdaters.forEach(::initUpdater)

        if (enabledUpdaters.isNotEmpty()) { // Only debug if we found updater, but we always want to start the task
            logger.debug {
                "Found ${enabledUpdaters.size} updaters" +
                    "\nUpdaters:\n\t" + enabledUpdaters.joinToString(separator = "\n\t") { updater ->
                    "${updater.name} - ${updater.providers.joinToString(separator = ", ") { it.name }}"
                }
            }
        }

        beginTask()
    }

    private fun initUpdater(updater: PluginUpdater) {
        updater.pluginInstance = pm.getPlugin(updater.name)
        updater.providers.retainAll { it !is NullUpdateProvider }

        when {
            updater.pluginInstance == null -> {
                logger.warn { "Couldn't find plugin with the name ${updater.name}" }
            }

            updater.providers.isEmpty() -> {
                logger.warn { "Updater ${updater.name} has no providers" }
            }

            else -> enabledUpdaters += updater
        }
    }

    private fun beginTask() {
        taskAsync(repeatDelay = 5.minutes) {
            try {
                for (updater in enabledUpdaters.toImmutableList()) {
                    if (updater.lastRun != null && now() < (updater.lastRun!! + updaterConfig.interval)) continue
                    logger.debug { "Checking ${updater.name} for updates" }

                    async {
                        updater.provider.query()
                        updater.lastRun = now()
                        checkForUpdate(updater).takeIf { it && updater.updateMode == UpdateMode.UPDATE }
                            ?.let { update(updater) }
                    }
                }
            } catch (e: ConcurrentModificationException) {
                logger.error(e) { "Error while checking for updates on ${Thread.currentThread().name}" }
            }
        }
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
                logger.warn {
                    """
                        No remote version found for ${updater.name}
                        You should contact the plugin author [${updater.pluginInstance?.description?.authors?.firstOrNull()}] about this!
                    """.trimIndent()
                }
                UpdateResult.FAILED_NO_VERSION
            }

            updater.localVersion.get().isFailure -> {
                logger.warn(updater.localVersion.get().exceptionOrNull(), SCOPE) { "Failed to get local version for ${updater.name} - ${updater.pluginInstance?.description?.version}" }
                UpdateResult.FAILED_VERSION
            }

            updater.localVersion.get().getOrThrow() >= updater.provider.latestVersion!! -> {
                versionAppend("${updater.name} is up to date or ahead of the latest remote version!", updater)
                UpdateResult.NO_UPDATE
            }

            updater.provider.providesMinecraftVersion -> {
                if (isCompatible(updater)) {
                    versionAppend("An update for ${updater.name} is available!", updater)
                    UpdateResult.UPDATE_FOUND
                } else {
                    UpdateResult.FAILED_VERSION_INCOMPATIBLE
                }
            }

            else -> {
                versionAppend("An update for ${updater.name} is available!", updater)
                UpdateResult.UPDATE_FOUND
            }
        }
        return updater.result!!.isSuccessful
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun update(updater: PluginUpdater): UpdateResult = coroutineScope {
        try {
            logger.debug { "Updating ${updater.name} with ${updater.provider.name}" }
            val hashGenerator = updater.provider.providesChecksum.instanceOrNull
            val downloadFile = updateFolder.resolve(updater.provider.latestFileName ?: error("No latest file name found!"))

            val backup = deferredAsync { backupPlugin(updater).takeIf { it.name.startsWith("FAILED") } }
            updater.result = downloadFile(updater.provider, downloadFile, hashGenerator)
            val hashCheck = deferredAsync { hashCheck(hashGenerator, downloadFile, updater.provider).takeIf { it.name.startsWith("FAILED") } }

            backup.await()
            hashCheck.await()

            when {
                backup.getCompleted() != null -> {
                    logger.debug { "Backup failed for ${updater.name} with ${updater.provider.name}" }
                    updater.result = UpdateResult.FAILED_BACKUP
                }

                hashCheck.getCompleted() != null -> {
                    logger.debug { "Hash check failed for ${updater.name} with ${updater.provider.name}" }
                    updater.result = UpdateResult.FAILED_CHECKSUM
                }

                updater.result?.name?.startsWith("FAILED") == true -> {
                    logger.warn { "Update failed for ${updater.name} with reason ${updater.provider.name}" }
                }

                else -> {
                    val (file, _) = unzip(downloadFile).also { if (it.second.name.startsWith("FAILED")) updater.result = it.second }
                    readyPlugin(updater, file ?: downloadFile)
                    updater.result = UpdateResult.SUCCESS
                    enabledUpdaters -= updater
                    disabledUpdaters += updater
                    if (updaterConfig.announceDownloadProgress) logger.info { "${updater.name} has been updated and readied for the next restart / reload." }
                }
            }
        } catch (e: Exception) {
            updater.result = UpdateResult.FAILED_NO_FILE
            logger.error(e) { "${updater.name} failed to update!" }
        }
        updater.result!!
    }

    private suspend fun downloadFile(
        provider: UpdateProvider,
        downloadFile: File,
        hashGenerator: MessageDigest?
    ): UpdateResult {
        if (downloadFile.exists()) {
            logger.debug { "File ${downloadFile.name} already exists, skipping download" }
            return UpdateResult.SUCCESS
        }

        val url = provider.latestFileURL ?: return UpdateResult.FAILED_NO_FILE
        var connection: HttpResponse? = null
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        return try {
            connection = provider.connect(url) ?: throw IOException("Could not connect to $url!")
            inputStream = hashGenerator?.let {
                DigestInputStream(connection.bodyAsChannel().toInputStream().buffered(), it)
            } ?: url.openStream().buffered()
            val fileLength = connection.bodyAsChannel().readLong()

            downloadFile.createNewFile()
            outputStream = downloadFile.outputStream()

            if (updaterConfig.announceDownloadProgress) {
                logger.info { "Started downloading update: ${provider.latestVersion}" }
            }

            withTimeout(15.seconds) {
                downloadWriter(inputStream, outputStream, fileLength)
            }

            UpdateResult.UPDATE_FOUND
        } catch (e: Exception) {
            return when (e) {
                is RequestTypeNotAvailableException -> {
                    logger.error(e) { "The update provider supplied invalid capability data!" }
                    UpdateResult.FAILED_DOWNLOAD
                }

                is NotSuccessfullyQueriedException -> {
                    logger.error(e) { "The update provider could not be queried!" }
                    UpdateResult.FAILED_CONNECTION
                }

                is IOException -> {
                    logger.error(e) { "The auto-updater tried to download a new update but was unsuccessful." }
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

    // TODO: Fix this
    private fun downloadWriter(
        inputStream: InputStream,
        outputStream: OutputStream,
        fileLength: Long
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
            logger.debug { "Downloading: $downloaded/$size" }
            outputStream.write(buffer, 0, count)

            if (!updaterConfig.announceDownloadProgress || fileLength <= 0) continue

            progress = (downloaded * percentPerByte).toInt()
            logger.debug { "Progress: $progress%" }
            if (progress % 10 != 0 && progress > lastProgress) {
                lastProgress = progress
                logger.info { "Downloading update: $progress% ($size)" }
            }
        }
    }

    private fun getSize(
        fileLength: Long
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

    private fun hashCheck(
        hashGenerator: MessageDigest?,
        downloadFile: File,
        updateProvider: UpdateProvider
    ): UpdateResult {
        if (updateProvider.latestChecksum != null && hashGenerator != null && updateProvider.providesChecksum == ChecksumType.MD5) {
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
                logger.warn {
                    """
                    The downloaded file for ${updateProvider.name} didn't match the expected checksum.
                    Expected: $targetMD5
                    Actual: $downloadMD5
                    """.trimIndent()
                }
                if (downloadFile.delete()) {
                    logger.info { "Deleted corrupted file." }
                } else {
                    logger.warn { "Could not delete corrupted file: ${downloadFile.absolutePath}" }
                }
                return UpdateResult.FAILED_CHECKSUM
            }
        }
        return UpdateResult.SUCCESS
    }

    private fun unzip(file: File): Pair<File?, UpdateResult> {
        if (file.name.endsWith(".zip")) {
            // Search for a file matching this plugins name in the zip
            var newFile: File? = null
            return try {
                val zipFile = ZipFile(file)
                val zipEntries = zipFile.entries()
                val pattern = Regex("^(\\A${plugin.name})?.+\\.jar$")
                var gotFile = false
                while (zipEntries.hasMoreElements() && !gotFile) {
                    val zipEntry = zipEntries.nextElement()
                    if (zipEntry.isDirectory || !zipEntry.name.matches(pattern)) continue

                    val tempFile = extractEntry(zipFile, zipEntry)

                    if (!file.delete()) logger.warn { "Couldn't delete old download file: ${file.absolutePath}" }
                    newFile = file.parentFile.resolve(zipEntry.name)
                    tempFile.renameTo(newFile)
                    gotFile = true
                }
                if (!gotFile) {
                    logger.warn {
                        """
                        Couldn't find a jar file in the zip file.
                        The zip file has been left in ${file.parentFile.absolutePath} for manual extraction.
                        """.trimIndent()
                    }
                }
                newFile to UpdateResult.SUCCESS
            } catch (e: IOException) {
                logger.warn(e) { "The auto-updater was unable to extract the downloaded zip file." }
                null to UpdateResult.FAILED_EXTRACTION
            }
        }
        return null to UpdateResult.SUCCESS
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
    ) {
        try {
            val newPath = file.copyTo(server.pluginsFolder.resolve(file.name), false)
            if (newPath.exists() && newPath.size() == file.size()) file.delete()

            get<DataService>().withDatabase {
                DataServiceImpl.DataHolder.new(updater.pluginInstance!!.name) {
                    newVersion = newPath.name
                    oldVersion = updater.localFile.substringAfterLast("/")
                }
            }
        } catch (e: ExposedSQLException) {
            logger.error(e) { "There was an error with the database while trying to prepare ${updater.name} for an update." }
        } catch (e: IOException) {
            logger.error(e) { "There was an IO error while trying to prepare ${updater.name} for an update." }
        }
    }

    private fun backupPlugin(updater: PluginUpdater): UpdateResult {
        val folder = updater.pluginInstance!!.dataFolder

        if (!folder.exists()) {
            logger.debug { "The plugin data folder does not exist: ${folder.absolutePath}" }
            return UpdateResult.SUCCESS
        }

        val data = Data(folder.size())

        if (data > updaterConfig.backups.maxSize) {
            logger.warn { "${updater.name}'s data folder is too large to backup (${data.humanReadableSize()}" }
            return UpdateResult.FAILED_BACKUP
        }

        val zipFile = updateFolder.resolve("${updater.name}-${updater.localVersion.get().getOrThrow().rawVersion}_backup_${now().toLocalDateTime(TimeZone.currentSystemDefault())}.zip")
        zipFile.createNewFile() // Shouldn't be a feasible way that this fails due to the name containing the current time
        ZipOutputStream(zipFile.outputStream().buffered()).use() {
            traverseDir(updater, it, folder)
        }

        return UpdateResult.SUCCESS
    }

    private fun traverseDir(
        updater: PluginUpdater,
        out: ZipOutputStream,
        directory: File,
        level: Int = 0
    ) {
        for (file in directory.listFiles() ?: return) {
            if (file.name in updater.ignored) continue

            if (file.isDirectory && level < 3) { // Don't go deeper than 3 levels
                traverseDir(updater, out, file, level + 1)
            } else {
                file.inputStream().buffered().use {
                    out.putNextEntry(ZipEntry(file.name))
                    BufferedInputStream(it).use { s -> s.copyTo(out, BUFFER_SIZE) }
                }
            }
        }
    }

    private fun isCompatible(updater: PluginUpdater): Boolean = try {
        val versions = updater.provider.latestMinecraftVersions?.map { str ->
            val version = str.replace(".", "_").let { "MC_$it" }
            MCVersion.valueOf(version)
        }
        val supported = versions?.any { MCVersion.currentVersion sameMajor it } ?: false
        if (!supported) {
            logger.info {
                val builder = StringBuilder()
                builder.append("An update for ${updater.name} is available, but it isn't compatible with the current version of Minecraft!")
                if (updaterConfig.verbose) {
                    builder.append("\nCurrent version: ${MCVersion.currentVersion}")
                    builder.append("\nSupported versions: ${versions?.joinToString { it.toString() }}")
                }
                builder.toString()
            }
        }
        supported
    } catch (e: Exception) {
        logger.warn(e) { "The auto-updater was unable to check the compatibility of the new version." }
        updater.result = UpdateResult.FAILED_VERSION_INCOMPATIBLE
        false
    }

    private fun versionAppend(mainString: String, updater: PluginUpdater) {
        if (updaterConfig.announceDownloadProgress && !updater.sentAvailable) {
            logger.info {
                val builder = StringBuilder()
                builder.append(mainString)
                if (updaterConfig.verbose) {
                    builder.append("\nLocal version: ${updater.localVersion.get().getOrNull()?.rawVersion}")
                    builder.append("\nRemote version: ${updater.provider.latestVersion?.rawVersion}")
                }
                builder.toString()
            }
            updater.sentAvailable = true
        }
    }

    public companion object {
        private const val SCOPE = "updaterService"
        private const val BUFFER_SIZE = 1024
        private val BYTE_NAMES by lazy { arrayOf("byte", "bytes", "kiB", "MiB", "GiB", "TiB", "PiB", "EiB") }
    }
}
