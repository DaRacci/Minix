package dev.racci.minix.api.autoscanner

import com.github.benmanes.caffeine.cache.Caffeine
import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import io.github.classgraph.ScanResult
import org.jetbrains.annotations.ApiStatus.AvailableSince
import org.jetbrains.annotations.ApiStatus.Experimental
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

private typealias FineTune = ClassGraph.() -> Unit

/**
 * A wrapper around [ClassGraph] with some extra functionality and caching.
 */
@Experimental
@AvailableSince("5.0.0")
public class Scanner private constructor(
    private val loaders: Collection<ClassLoader>,
    private val path: String?,
    private val jarLeaf: String?,
    private val excludes: Collection<String>,
    private val fineTune: FineTune?
) {

    /**
     * The [ScanResult] of this [Scanner]
     * If the [ScanResult] is not cached, it will be computed and cached.
     */
    public fun getResult(): ScanResult {
        val cacheKey = CacheKey(loaders, jarLeaf, path, fineTune)

        return cache.get(cacheKey) { _ ->
            ClassGraph().apply {
                if (loaders.isNotEmpty()) {
                    ignoreParentClassLoaders()
                    overrideClassLoaders(*loaders.toTypedArray())
                }
                if (jarLeaf != null) acceptJars(jarLeaf)
                if (path != null) acceptPaths(path)
                if (excludes.isNotEmpty()) rejectClasses(*excludes.toTypedArray())

                enableClassInfo()
                enableExternalClasses()
                disableNestedJarScanning()
                disableRuntimeInvisibleAnnotations()
                removeTemporaryFilesAfterScan()
                fineTune?.invoke(this)
            }.scan(4)
        }
    }

    /**
     * @param annotation The target annotation class.
     * @param filter A filter to refine the resulting set.
     * @return A [Sequence] of all the classes that are annotated with the given annotation.
     */
    public fun withAnnotation(
        annotation: KClass<out Annotation>,
        filter: ClassInfo.() -> Boolean = { true }
    ): List<ClassInfo> = getResult().getClassesWithAnnotation(annotation.java).filter(filter)

    /**
     * @param superclass The target superclass.
     * @param filter A filter to refine the resulting set.
     * @return A [Sequence] of all the classes that are subclasses of the given superclass.
     */
    public fun withSuperclass(
        superclass: KClass<*>,
        filter: ClassInfo.() -> Boolean = { true }
    ): Sequence<ClassInfo> = getResult().getSubclasses(superclass.java).asSequence().filter(filter)

    /**
     * @param target The target interface.
     * @param filter A filter to refine the resulting set.
     * @return A [Sequence] of all the classes that implement the given interface.
     */
    public fun withInterface(
        target: KClass<*>,
        filter: ClassInfo.() -> Boolean = { true }
    ): Sequence<ClassInfo> = getResult().getClassesImplementing(target.java).asSequence().filter(filter)

    /**
     * @param superclass The target superclass.
     * @param filter A filter to refine the resulting set.
     * @return A [Sequence] of all the classes that are subclasses of the given superclass.
     */
    public fun <T : Any> withSuperclassKClass(
        superclass: KClass<T>,
        filter: ClassInfo.() -> Boolean = { true }
    ): Sequence<KClass<T>> = withSuperclass(superclass, filter).map { classInfo -> classInfo.loadClass(superclass.java, true).kotlin }

    /**
     * @param target The target interface.
     * @param filter A filter to refine the resulting set.
     * @return A [Sequence] of all the classes that implement the given interface.
     */
    public fun <T : Any> withInterfaceKClass(
        target: KClass<T>,
        filter: ClassInfo.() -> Boolean = { true }
    ): Sequence<KClass<T>> = withInterface(target, filter).map { classInfo -> classInfo.loadClass(target.java, true).kotlin }

    /**
     * @param T The reified type of the annotation.
     * @param filter A filter to refine the resulting set.
     * @return A [Sequence] of all the classes that are annotated with the given annotation.
     */
    public inline fun <reified T : Annotation> withAnnotation(
        noinline filter: ClassInfo.() -> Boolean = { true }
    ): List<ClassInfo> = withAnnotation(T::class, filter)

    /**
     * @param T The reified type of the superclass.
     * @param filter A filter to refine the resulting set.
     * @return A [Sequence] of all the classes that are subclasses of the given superclass.
     */
    public inline fun <reified T : Any> withSuperclass(
        noinline filter: ClassInfo.() -> Boolean = { true }
    ): Sequence<ClassInfo> = withSuperclass(T::class, filter)

    /**
     * @param T The reified type of the interface.
     * @param filter A filter to refine the resulting set.
     * @return A [Sequence] of all the classes that implement the given interface.
     */
    public inline fun <reified T : Any> withInterface(
        noinline filter: ClassInfo.() -> Boolean = { true }
    ): Sequence<ClassInfo> = withInterface(T::class, filter)

    /**
     * @param T The reified type of the superclass.
     * @param filter A filter to refine the resulting set.
     * @return A [Sequence] of all the classes that are subclasses of the given superclass.
     */
    public inline fun <reified T : Any> withSuperclassKClass(
        noinline filter: ClassInfo.() -> Boolean = { true }
    ): Sequence<KClass<T>> = withSuperclassKClass(T::class, filter)

    /**
     * @param T The reified type of the interface.
     * @param filter A filter to refine the resulting set.
     * @return A [Sequence] of all the classes that implement the given interface.
     */
    public inline fun <reified T : Any> withInterfaceKClass(
        noinline filter: ClassInfo.() -> Boolean = { true }
    ): Sequence<KClass<T>> = withInterfaceKClass(T::class, filter)

    public companion object {
        private val cache = Caffeine.newBuilder()
            .maximumSize(25)
            .expireAfterWrite(15, TimeUnit.SECONDS)
            .removalListener<CacheKey, ScanResult> { _, value, _ -> value?.close() }
            .build<CacheKey, ScanResult>()

        private data class CacheKey(
            val loaders: Collection<ClassLoader>,
            val jarLeaf: String?,
            val path: String?,
            val fineTune: FineTune?
        )

        public fun of(
            path: String? = null,
            jarLeaf: String? = null,
            loaders: Collection<ClassLoader> = emptyList(),
            excludes: Collection<String> = emptyList(),
            fineTune: FineTune? = null
        ): Scanner = Scanner(loaders, path, jarLeaf, excludes, fineTune)

        public fun ofJar(
            jarLeaf: String,
            excludes: Collection<String> = emptyList(),
            fineTune: FineTune? = null
        ): Scanner = Scanner(emptyList(), null, jarLeaf, excludes, fineTune)

        /**
         * Creates a new instance of the scanner with the callers class loader,
         * the callers jar leaf, and the callers package path.
         *
         * @param refinedSubpackage Appends this path to the callers package path
         * @param excludes Excludes these classes from the scan
         * @return A new instance of the scanner
         */
        public fun callerBased(
            refinedSubpackage: String? = null,
            excludes: Collection<String> = emptyList(),
            fineTune: FineTune? = null
        ): Scanner = with(StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).callerClass) {
            Scanner(
                listOf(this.classLoader),
                this.packageName.replace('.', '/').let { path -> if (!refinedSubpackage.isNullOrBlank()) "$path/$refinedSubpackage" else path },
                this.protectionDomain.codeSource.location.file,
                excludes,
                fineTune
            )
        }

        /**
         * Creates a new instance of the scanner that scans all classloaders and all paths.
         */
        public fun global(fineTune: FineTune? = null): Scanner = Scanner(
            emptyList(),
            null,
            null,
            emptyList(),
            fineTune
        )
    }
}
