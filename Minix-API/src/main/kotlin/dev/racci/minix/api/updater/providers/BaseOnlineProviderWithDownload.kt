package dev.racci.minix.api.updater.providers

import dev.racci.minix.api.updater.Version
import java.net.URL

abstract class BaseOnlineProviderWithDownload : BaseOnlineProvider {

    protected var lastResult: UpdateFile? = null
    override val latestFileName: String
        get() = lastResult?.fileName ?: throw NotSuccessfullyQueriedException()
    override val latestName: String
        get() = lastResult?.name ?: throw NotSuccessfullyQueriedException()
    override val latestVersion: Version
        get() = lastResult?.version ?: throw NotSuccessfullyQueriedException()
    override val latestFileURL: URL
        get() = lastResult?.downloadURL ?: throw NotSuccessfullyQueriedException()

    protected constructor() : super()
    protected constructor(userAgent: String) : super(userAgent)
}
