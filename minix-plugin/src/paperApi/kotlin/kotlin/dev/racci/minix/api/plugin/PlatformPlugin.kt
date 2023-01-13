package dev.racci.minix.api.plugin

import dev.racci.minix.api.exceptions.WrappingException
import dev.racci.minix.api.extensions.collections.findKCallable
import dev.racci.minix.api.extensions.reflection.accessGet
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.utils.suspendBlockingLazy
import dev.racci.minix.api.wrappers.WrapperCompanion
import dev.racci.minix.data.Version
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.PluginClassLoader
import org.koin.core.qualifier.Qualifier
import java.nio.file.Path
import kotlin.reflect.full.declaredMemberProperties

public actual interface PlatformPlugin : Qualifier, Comparable<PlatformPlugin> {
    public actual override val value: String
    public actual val version: Version
    public actual val dataFolder: Path
    public actual val dependencies: ImmutableSet<String>
    public actual val platformClassLoader: ClassLoader

    override fun compareTo(other: PlatformPlugin): Int {
        if ((other.dependencies).contains(this.value)) return 1
        if ((this.dependencies).contains(other.value)) return -1
        return 0
    }

    public actual companion object : WrapperCompanion<PlatformPlugin> {
        actual override fun wrapped(obj: Any): PlatformPlugin {
            if (obj is PlatformPlugin) return obj
            if (obj !is JavaPlugin) throw WrappingException(null, obj, JavaPlugin::class)

            return object : PlatformPlugin {
                override val value: String = obj.name
                override val version: Version by lazy { Version.parseString(obj.description.version) }
                override val dataFolder: Path = obj.dataFolder.toPath()
                override val dependencies: ImmutableSet<String> by lazy { (obj.description.depend + obj.description.softDepend).toImmutableSet() }
                override val platformClassLoader: ClassLoader by suspendBlockingLazy {
                    JavaPlugin::class.declaredMemberProperties
                        .findKCallable("classLoader")
                        .fold(
                            { error("Couldn't get the classLoader property, unsupported version?") },
                            { prop -> prop.accessGet(obj).castOrThrow<PluginClassLoader>() }
                        )
                }
            }
        }
    }
}
