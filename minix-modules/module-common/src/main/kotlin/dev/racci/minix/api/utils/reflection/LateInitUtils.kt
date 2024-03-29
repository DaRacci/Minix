package dev.racci.minix.api.utils.reflection

import dev.racci.minix.api.extensions.reflection.accessGet
import dev.racci.minix.api.extensions.reflection.accessSet
import org.apiguardian.api.API
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

@API(status = API.Status.EXPERIMENTAL, since = "4.2.0")
public object LateInitUtils {

    public suspend inline fun <T> getLateInit(
        prop: KProperty0<T>
    ): T? {
        if (!prop.isLateinit) return prop.get()
        return runCatching { prop.accessGet() }.getOrNull()
    }

    public suspend inline fun <T> getOrSet(
        prop: KMutableProperty0<T>,
        value: () -> T
    ): T {
        if (!prop.isLateinit) return prop.get()
        return runCatching { prop.accessGet() }.getOrNull() ?: value().also { prop.accessSet(it) }
    }
}
