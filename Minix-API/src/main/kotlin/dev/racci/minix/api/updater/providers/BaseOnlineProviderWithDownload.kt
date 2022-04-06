package dev.racci.minix.api.updater.providers

import dev.racci.minix.api.updater.Version
import java.net.URL

abstract class BaseOnlineProviderWithDownload : BaseOnlineProvider {

    protected var lastResult: UpdateFile? = null
    override val latestFileName: String? get() = lastResult?.fileName
    override val latestName: String? get() = lastResult?.name
    override val latestVersion: Version? get() = lastResult?.version
    override val latestFileURL: URL? get() = lastResult?.downloadURL

    protected constructor() : super()
    protected constructor(userAgent: String) : super(userAgent)
}
