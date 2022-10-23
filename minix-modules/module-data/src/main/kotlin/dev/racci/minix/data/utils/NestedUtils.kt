package dev.racci.minix.data.utils

import dev.racci.minix.api.extensions.reflection.accessGet
import dev.racci.minix.api.extensions.reflection.castOrThrow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import org.apiguardian.api.API
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubclassOf

@API(status = API.Status.EXPERIMENTAL, since = "4.1.0")
public object NestedUtils {

    /**
     * Returns a sequence of nested classes from the given class
     * This includes the class itself.
     *
     * @param baseKClass The starting class.
     * @param curDepth The current search depth.
     * @param maxDepth The maximum search depth.
     * @return A flow of nested classes.
     */
    public fun getNestedClasses(
        baseKClass: KClass<*>,
        curDepth: Int = 0,
        maxDepth: Int = 9
    ): Flow<KClass<*>> {
        if (curDepth > maxDepth) return emptyFlow()

        return flow {
            emit(baseKClass)
            baseKClass.nestedClasses.forEach { nested ->
                emit(nested)
                emitAll(getNestedClasses(nested, curDepth + 1, maxDepth))
            }
        }
    }

    /**
     * Gets all instances of [R] by traversing the given class and all of its nested classes.
     *
     * @param baseInstance The starting instance.
     * @param curDepth The current search depth.
     * @param maxDepth The maximum search depth.
     * @param R The instance type to find.
     * @return A flow of instances of [R].
     */
    public fun <R : Any> getNestedInstances(
        clazz: KClass<*>,
        baseInstance: Any,
        curDepth: Int = 0,
        maxDepth: Int = 9
    ): Flow<R> {
        if (curDepth > maxDepth) return emptyFlow()

        return flow {
            when {
                baseInstance::class.java.isAnonymousClass -> Unit // Inner anon object
                baseInstance::class.isSubclassOf(clazz) -> Unit // Inner class
                baseInstance::class.java.packageName.startsWith(clazz.java.packageName) -> Unit // Same package
                baseInstance is PropertyFinder<*> -> Unit // Property finder
                else -> return@flow // Ignore
            }

            baseInstance::class.declaredMemberProperties.asFlow()
                .filterIsInstance<KProperty1<Any, Any?>>()
                .mapNotNull { it.accessGet(baseInstance) }
                .collect { instance ->
                    try {
                        emit(instance.castOrThrow())
                    } catch (e: ClassCastException) { /* no-op */ }

                    emitAll(getNestedInstances(clazz, instance, curDepth + 1, maxDepth))
                }
        }
    }
}
