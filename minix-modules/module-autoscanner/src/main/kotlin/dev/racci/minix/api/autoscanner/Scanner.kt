package dev.racci.minix.api.autoscanner

import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import io.github.classgraph.ScanResult
import org.jetbrains.annotations.Contract
import kotlin.reflect.KClass

/**
 * A wrapper around [ClassGraph] with some extra functionality and caching.
 */
public data class Scanner(
    private val loaders: Collection<ClassLoader>,
    private val path: String? = null,
    private val jarLeaf: String? = null,
    @PublishedApi internal val excludes: Collection<String> = emptyList()
) {

    /**
     * The [ScanResult] of this [Scanner]
     * If the [ScanResult] is not cached, it will be computed and cached.
     */
    public fun getResult(): ScanResult {
        val cacheKey = CacheKey(loaders, jarLeaf, path)
        return cache.computeIfAbsent(cacheKey) { _ ->
            ClassGraph().apply {
                loaders.forEach(::addClassLoader)
                if (jarLeaf != null) acceptJars(jarLeaf)
                if (path != null) acceptPaths(path)
                if (excludes.isNotEmpty()) rejectClasses(*excludes.toTypedArray())

                enableClassInfo()
                enableExternalClasses()
                disableNestedJarScanning()
                disableRuntimeInvisibleAnnotations()
                removeTemporaryFilesAfterScan()
            }.scan(4)
        }
    }

    /**
     * @param T The reified type of the annotation.
     * @param filter A filter to refine the resulting set.
     * @return A list of all the classes that are annotated with the given annotation.
     */
    public inline fun <reified T : Annotation> withAnnotation(
        noinline filter: ClassInfo.() -> Boolean = { true }
    ): List<ClassInfo> = getResult().getClassesWithAnnotation(T::class.java).filter(filter)

    /**
     * @param T The reified type of the superclass.
     * @param filter A filter to refine the resulting set.
     * @return A list of all the classes that are subclasses of the given superclass.
     */
    public inline fun <reified T : Any> withSuperclass(
        noinline filter: ClassInfo.() -> Boolean = { true }
    ): List<ClassInfo> = getResult().getSubclasses(T::class.java.name).filter(filter)

    /**
     * @param T The reified type of the interface.
     * @param filter A filter to refine the resulting set.
     * @return A list of all the classes that implement the given interface.
     */
    public inline fun <reified T : Any> withInterface(
        noinline filter: ClassInfo.() -> Boolean = { true }
    ): List<ClassInfo> = getResult().getClassesImplementing(T::class.java.name).filter(filter)

    /**
     * @param T The reified type of the superclass.
     * @param filter A filter to refine the resulting set.
     * @return A list of all the classes that are subclasses of the given superclass.
     */
    public inline fun <reified T : Any> withSuperclassKClass(
        noinline filter: ClassInfo.() -> Boolean = { true }
    ): List<KClass<T>> = withSuperclass<T>(filter).map { classInfo -> classInfo.loadClass(T::class.java, true).kotlin }

    /**
     * @param T The reified type of the interface.
     * @param filter A filter to refine the resulting set.
     * @return A list of all the classes that implement the given interface.
     */
    public inline fun <reified T : Any> withInterfaceKClass(
        noinline filter: ClassInfo.() -> Boolean = { true }
    ): List<KClass<T>> = withInterface<T>(filter).map { classInfo -> classInfo.loadClass(T::class.java, true).kotlin }

    public companion object {
        private val cache = mutableMapOf<CacheKey, ScanResult>()

        private data class CacheKey(
            val loaders: Collection<ClassLoader>,
            val jarLeaf: String?,
            val path: String?
        )

        /**
         * Creates a new instance of the scanner with the callers class loader,
         * the callers jar leaf, and the callers package path.
         *
         * @param refinedSubpackage Appends this path to the callers package path
         * @param excludes Excludes these classes from the scan
         * @return A new instance of the scanner
         */
        @Contract("_, _, _ -> new", pure = true)
        public fun callerBased(
            refinedSubpackage: String? = null,
            excludes: Collection<String> = emptyList()
        ): Scanner = with(StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).callerClass) {
            return Scanner(
                listOf(this.classLoader),
                this.packageName.replace('.', '/').let { path -> if (!refinedSubpackage.isNullOrBlank()) "$path/$refinedSubpackage" else path },
                this.protectionDomain.codeSource.location.file,
                excludes
            )
        }
    }
}
