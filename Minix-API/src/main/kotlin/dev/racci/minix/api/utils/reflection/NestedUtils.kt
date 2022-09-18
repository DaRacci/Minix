package dev.racci.minix.api.utils.reflection

import dev.racci.minix.api.utils.PropertyFinder
import dev.racci.minix.api.utils.UtilObject
import dev.racci.minix.api.utils.accessReturn
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubclassOf

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
        maxDepth: Int = 9,
        qualifier: String? = null
    ): Sequence<R> {
        if (curDepth > maxDepth) return emptySequence()

        logger.trace { "Nested classes of ${clazz.qualifiedName} -> ${getNestedClasses(clazz).joinToString(", ") { it.qualifiedName!! }}" }

//        if (curDepth > 0 && instance::class.qualifiedName?.startsWith(qualifier) == false) {
//            logger.trace { "Skipping ${instance::class.qualifiedName} because it doesn't start with $qualifier" }
//            return emptySequence()
//        }

//        val queue = Queues.newSynchronousQueue<Any>()
//
//        return sequence {
//            while (queue.isNotEmpty()) {
//                val instance = queue.remove()
//
//                if (instance::class.qualifiedName?.startsWith(qualifier) == false) {
//                    logger.trace { "Skipping ${instance::class.qualifiedName} because it doesn't start with $qualifier" }
//                    continue
//                }
//
//                val nonNulls = instance::class.declaredMemberProperties
//                    .filterIsInstance<KProperty1<Any, Any?>>()
//                    .mapNotNull { it.accessReturn { get(instance) } }
//
//                try {
//                    yield(instance as R)
//                } catch (e: ClassCastException) { logger.trace(e) }
//
//                queue.addAll(nonNulls)
//            }
//        }

        return sequence {
            when {
                baseInstance::class.java.isAnonymousClass -> logger.trace { "${baseInstance::class.qualifiedName} is anonymous." } // Inner anon object
                baseInstance::class.isSubclassOf(clazz) -> logger.trace { "${baseInstance::class.qualifiedName} is a subclass of ${clazz.qualifiedName}" } // Inner class
                baseInstance::class.java.`package`.name.startsWith(clazz.java.`package`.name) -> logger.trace { "${baseInstance::class.qualifiedName} is in the same package as ${clazz.qualifiedName}" } // Same package
                baseInstance is PropertyFinder<*> -> logger.trace { "${baseInstance::class.qualifiedName} is a property finder." } // Property finder
                else -> return@sequence logger.trace { "Skipping ${baseInstance::class.qualifiedName}" }
            }

            val nonNulls = baseInstance::class.declaredMemberProperties
                .filterIsInstance<KProperty1<Any, Any?>>()
                .mapNotNull { it.accessReturn { get(baseInstance) } }

            for (instance in nonNulls) {
                try {
                    yield(instance as R)
                } catch (e: ClassCastException) { logger.trace(e) }

                yieldAll(getNestedInstances(clazz, instance, curDepth + 1, maxDepth))
            }
        }
    }
}
