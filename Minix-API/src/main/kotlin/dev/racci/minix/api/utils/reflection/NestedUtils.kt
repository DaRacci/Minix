package dev.racci.minix.api.utils.reflection

import dev.racci.minix.api.extensions.reflection.accessGet
import dev.racci.minix.api.utils.PropertyFinder
import dev.racci.minix.api.utils.UtilObject
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
object NestedUtils : UtilObject {

    /**
     * Returns a sequence of nested classes from the given class
     * This includes the class itself.
     *
     * @param baseKClass The starting class.
     * @param curDepth The current search depth.
     * @param maxDepth The maximum search depth.
     * @return A flow of nested classes.
     */
    fun getNestedClasses(
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
    fun <R : Any> getNestedInstances(
        clazz: KClass<*>,
        baseInstance: Any,
        curDepth: Int = 0,
        maxDepth: Int = 9
    ): Flow<R> {
        if (curDepth > maxDepth) return emptyFlow()

        return flow {
            when {
                baseInstance::class.java.isAnonymousClass -> logger.trace { "${baseInstance::class.qualifiedName} is anonymous." } // Inner anon object
                baseInstance::class.isSubclassOf(clazz) -> logger.trace { "${baseInstance::class.qualifiedName} is a subclass of ${clazz.qualifiedName}" } // Inner class
                baseInstance::class.java.packageName.startsWith(clazz.java.packageName) -> logger.trace { "${baseInstance::class.qualifiedName} is in the same package as ${clazz.qualifiedName}" } // Same package
                baseInstance is PropertyFinder<*> -> logger.trace { "${baseInstance::class.qualifiedName} is a property finder." } // Property finder
                else -> return@flow // Ignore
            }

            baseInstance::class.declaredMemberProperties.asFlow()
                .filterIsInstance<KProperty1<Any, Any?>>()
                .mapNotNull { it.accessGet(baseInstance) }
                .collect { instance ->
                    try {
                        emit(instance as R)
                    } catch (e: ClassCastException) { logger.trace(e) }

                    emitAll(getNestedInstances(clazz, instance, curDepth + 1, maxDepth))
                }
        }

//        return sequence {
//            when {
//                baseInstance::class.java.isAnonymousClass -> logger.trace { "${baseInstance::class.qualifiedName} is anonymous." } // Inner anon object
//                baseInstance::class.isSubclassOf(clazz) -> logger.trace { "${baseInstance::class.qualifiedName} is a subclass of ${clazz.qualifiedName}" } // Inner class
//                baseInstance::class.java.packageName.startsWith(clazz.java.packageName) -> logger.trace { "${baseInstance::class.qualifiedName} is in the same package as ${clazz.qualifiedName}" } // Same package
//                baseInstance is PropertyFinder<*> -> logger.trace { "${baseInstance::class.qualifiedName} is a property finder." } // Property finder
//                else -> return@sequence // Ignore
//            }
//
//            val nonNulls = baseInstance::class.declaredMemberProperties.asFlow()
//                .filterIsInstance<KProperty1<Any, Any?>>()
//                .mapNotNull { it.accessGet(baseInstance) }
//                .flowOn()
//
//            nonNulls.collect { instance ->
//                try {
//                    yield(instance as R)
//                } catch (e: ClassCastException) { logger.trace(e) }
//
//                yieldAll(getNestedInstances(clazz, instance, curDepth + 1, maxDepth))
//            }
//        }
    }
}
