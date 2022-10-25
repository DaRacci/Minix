package dev.racci.minix.api.autoscanner

import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import io.github.classgraph.ScanResult

public class Scanner(
    private val loaders: Collection<ClassLoader>,
    private val jarLeaf: String? = null,
    private val path: String? = null,
    @PublishedApi
    internal val excludes: Collection<String> = emptyList()
) {

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

    public inline fun <reified T : Annotation> withAnnotation(): List<ClassInfo> {
        return getResult().getClassesWithAnnotation(T::class.java)
    }

    public inline fun <reified T : Annotation> withAnnotation(
        noinline filter: ClassInfo.() -> Boolean
    ): List<ClassInfo> = withAnnotation<T>().filter(filter)

    private companion object {
        data class CacheKey(
            val loaders: Collection<ClassLoader>,
            val jarLeaf: String?,
            val path: String?
        )

        val cache = mutableMapOf<CacheKey, ScanResult>()
    }
}
