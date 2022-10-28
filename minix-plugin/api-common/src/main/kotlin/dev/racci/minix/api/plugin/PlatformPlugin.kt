package dev.racci.minix.api.plugin

import dev.racci.minix.api.wrappers.WrapperCompanion
import dev.racci.minix.data.Version
import org.koin.core.qualifier.Qualifier
import java.nio.file.Path

public expect interface PlatformPlugin : Qualifier {
    public override val value: String

    public val version: Version

    public val dataFolder: Path

    public val platformClassLoader: ClassLoader

    public companion object : WrapperCompanion<PlatformPlugin> {
        override fun wrapped(obj: Any): PlatformPlugin
    }
}
