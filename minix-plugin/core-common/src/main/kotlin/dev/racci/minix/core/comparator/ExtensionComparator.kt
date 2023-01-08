package dev.racci.minix.core.comparator

import com.github.benmanes.caffeine.cache.Caffeine
import dev.racci.minix.api.annotations.Depends
import dev.racci.minix.api.extension.Extension
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

// Comparator for sorting extensions by dependencies
public object ExtensionComparator : Comparator<Extension<*>> {
    private val dependencies = Caffeine.newBuilder()
        .maximumSize(100)
        .expireAfterAccess(10, TimeUnit.SECONDS)
        .build<Extension<*>, List<KClass<*>>> { it::class.findAnnotation<Depends>()?.dependencies?.toList() ?: emptyList() }

    override fun compare(
        o1: Extension<*>,
        o2: Extension<*>
    ): Int {
        if (dependencies[o1].contains(o2::class)) return 1
        if (dependencies[o2].contains(o1::class)) return -1
        return 0
    }
}
