package dev.racci.minix.api.services

import com.github.benmanes.caffeine.cache.LoadingCache
import dev.racci.minix.api.annotations.MappedConfig
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.MissingAnnotationException
import dev.racci.minix.api.utils.safeCast
import dev.racci.minix.api.utils.unsafeCast
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.io.File
import kotlin.reflect.KClass

abstract class DataService : Extension<Minix>() {

    /**
     * Holds and generates new [HoconConfigurationLoader] for Classes annotated with [MappedConfig].
     *
     * @throws MissingAnnotationException if the class is not annotated with [MappedConfig]
     * @throws MissingAnnotationException if the class is not a annotated with [ConfigSerializable]
     * @throws IllegalArgumentException if the class is not a subtype of [MinixPlugin]
     */
    @get:Throws(MissingAnnotationException::class, IllegalArgumentException::class)
    abstract val configurateLoaders: LoadingCache<KClass<*>, HoconConfigurationLoader>

    /**
     * Holds and loads the configurations for Classes annotated with [MappedConfig].
     */
    abstract val configurations: LoadingCache<KClass<*>, Any>

    inline operator fun <reified T : Any> get(clazz: KClass<T> = T::class): T = configurations[clazz].unsafeCast()

    inline fun <reified T : Any> getOrNull(): T? = configurations[T::class].safeCast()

    abstract suspend fun getConfigurateLoader(file: File): HoconConfigurationLoader
}
