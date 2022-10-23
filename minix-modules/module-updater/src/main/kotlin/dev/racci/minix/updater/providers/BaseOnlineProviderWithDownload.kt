package dev.racci.minix.updater.providers

import dev.racci.minix.api.utils.Version
import java.net.URL

public abstract class BaseOnlineProviderWithDownload : BaseOnlineProvider {

    protected var lastResult: UpdateProvider.UpdateFile? = null
    public override val latestFileName: String? get() = lastResult?.fileName
    public override val latestName: String? get() = lastResult?.name
    public override val latestVersion: Version? get() = lastResult?.version
    public override val latestFileURL: URL? get() = lastResult?.downloadURL

    protected constructor() : super()
    protected constructor(userAgent: String) : super(userAgent)
}
