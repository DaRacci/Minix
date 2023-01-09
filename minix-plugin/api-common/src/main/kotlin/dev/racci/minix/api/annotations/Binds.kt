package dev.racci.minix.api.annotations

import org.jetbrains.annotations.ApiStatus.AvailableSince
import org.jetbrains.annotations.ApiStatus.Experimental
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@Experimental
@AvailableSince("5.0.0")
@MinixReflectiveAPI
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
public annotation class Binds(val classes: Array<KClass<*>>) {
    public companion object {

        public inline fun <reified T : Any> binds(): Array<KClass<*>> = binds(T::class)

        public fun binds(kClass: KClass<*>): Array<KClass<*>> {
            val binds = kClass.findAnnotation<Binds>()?.classes.orEmpty()
            return Array(binds.size + 1) {
                if (it == binds.size) kClass
                else binds[it]
            }
        }
    }
}
