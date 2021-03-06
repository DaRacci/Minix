package dev.racci.minix.api.services

import com.github.benmanes.caffeine.cache.LoadingCache
import dev.racci.minix.api.annotations.MappedConfig
import dev.racci.minix.api.exceptions.MissingAnnotationException
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.safeCast
import dev.racci.minix.api.utils.unsafeCast
import org.spongepowered.configurate.CommentedConfigurationNode
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
    abstract val configurations: LoadingCache<KClass<*>, Pair<Any, CommentedConfigurationNode>>

    inline fun <reified T> get(): T = configurations[T::class].first.unsafeCast()

    inline fun <reified T : Any> getOrNull(): T? = configurations[T::class].first.safeCast()

    inline fun <reified T : Any> inject(): Lazy<T> = lazy(::get)

    abstract suspend fun <T : Any> getConfigurateLoader(clazz: KClass<T>, file: File): HoconConfigurationLoader

    companion object : ExtensionCompanion<DataService>() {
        inline fun <reified T> Lazy<DataService>.inject(): Lazy<T> = lazy(value::get)
    }
}
