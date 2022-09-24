package dev.racci.minix.api.utils.reflection

import dev.racci.minix.api.extensions.reflection.accessGet
import dev.racci.minix.api.utils.PropertyFinder
import dev.racci.minix.api.utils.UtilObject
import org.apiguardian.api.API
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubclassOf

@API(status = API.Status.EXPERIMENTAL, since = "4.1.0")
object NestedUtils : UtilObject by UtilObject {

    /**
     * Returns a sequence of nested classes from the given class
     * This includes the class itself.
     *
     * @param baseKClass The starting class.
     * @param curDepth The current search depth.
     * @param maxDepth The maximum search depth.
     * @return A sequence of nested classes.
     */
    fun getNestedClasses(
        baseKClass: KClass<*>,
        curDepth: Int = 0,
        maxDepth: Int = 9
    ): Sequence<KClass<*>> {
        if (curDepth > maxDepth) return emptySequence()

        return sequence {
            yield(baseKClass)
            yieldAll(baseKClass.nestedClasses)
            baseKClass.nestedClasses.forEach { nested ->
                yieldAll(getNestedClasses(nested, curDepth + 1, maxDepth))
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
     * @return
     */
    fun <R : Any> getNestedInstances(
        clazz: KClass<*>,
        baseInstance: Any,
        curDepth: Int = 0,
        maxDepth: Int = 9
    ): Sequence<R> {
        if (curDepth > maxDepth) return emptySequence()

        return sequence {
            when {
                baseInstance::class.java.isAnonymousClass -> logger.trace { "${baseInstance::class.qualifiedName} is anonymous." } // Inner anon object
                baseInstance::class.isSubclassOf(clazz) -> logger.trace { "${baseInstance::class.qualifiedName} is a subclass of ${clazz.qualifiedName}" } // Inner class
                baseInstance::class.java.packageName.startsWith(clazz.java.packageName) -> logger.trace { "${baseInstance::class.qualifiedName} is in the same package as ${clazz.qualifiedName}" } // Same package
                baseInstance is PropertyFinder<*> -> logger.trace { "${baseInstance::class.qualifiedName} is a property finder." } // Property finder
                else -> return@sequence // Ignore
            }

            val nonNulls = baseInstance::class.declaredMemberProperties
                .filterIsInstance<KProperty1<Any, Any?>>()
                .mapNotNull { it.accessGet(baseInstance) }

            for (instance in nonNulls) {
                try {
                    yield(instance as R)
                } catch (e: ClassCastException) { logger.trace(e) }

                yieldAll(getNestedInstances(clazz, instance, curDepth + 1, maxDepth))
            }
        }
    }
}
