package dev.racci.minix.api.extensions.collections

import arrow.core.Option
import arrow.core.Validated
import arrow.core.filterIsInstance
import dev.racci.minix.api.utils.collections.CollectionUtils
import kotlin.reflect.KCallable
import kotlin.reflect.KFunction
import kotlin.reflect.KFunction0
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KMutableProperty2
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1
import kotlin.reflect.KProperty2

/** @see CollectionUtils.If.ifEmpty */
public suspend inline fun <R, C : Collection<*>> C.ifEmpty(
    crossinline action: suspend C.() -> R
): Option<R> = CollectionUtils.If.ifEmpty(this, action)

/** @see CollectionUtils.If.ifNotEmpty */
public suspend inline fun <R, C : Collection<*>> C.ifNotEmpty(
    crossinline action: suspend C.() -> R
): Option<R> = CollectionUtils.If.ifNotEmpty(this, action)

/** @see CollectionUtils.Contains.containsString */
public fun Collection<String>.contains(
    value: String,
    ignoreCase: Boolean = false
): Boolean = CollectionUtils.Contains.containsString(this, value, ignoreCase)

/** @see CollectionUtils.Get.getCast */
public fun <T : Any> Collection<*>.getCast(
    index: Int
): Validated<Throwable, T> = CollectionUtils.Get.getCast(this, index)

/** @see CollectionUtils.Find.findKCallable */
public fun <T : KCallable<*>> Collection<T>.findKCallable(
    name: String,
    ignoreCase: Boolean = false
): Option<T> = CollectionUtils.Find.findKCallable(this, name, ignoreCase)

/** @see CollectionUtils.Find.findKCallable */
@JvmName("findKProperty0")
public inline fun <reified R> Collection<KProperty0<*>>.findKProperty(
    name: String,
    ignoreCase: Boolean = false
): Option<KProperty0<R>> = CollectionUtils.Find.findKCallable(this, name, ignoreCase).filterIsInstance()

/** @see CollectionUtils.Find.findKCallable */
@JvmName("findKProperty1")
public inline fun <reified R> Collection<KProperty1<*, *>>.findKProperty(
    name: String,
    ignoreCase: Boolean = false
): Option<KProperty1<Any, R>> = CollectionUtils.Find.findKCallable(this, name, ignoreCase).filterIsInstance()

/** @see CollectionUtils.Find.findKCallable */
@JvmName("findKProperty2")
public inline fun <reified R> Collection<KProperty2<*, *, *>>.findKProperty(
    name: String,
    ignoreCase: Boolean = false
): Option<KProperty2<*, *, R>> = CollectionUtils.Find.findKCallable(this, name, ignoreCase).filterIsInstance()

/** @see CollectionUtils.Find.findKCallable */
@JvmName("findKPropertyMutable0")
public inline fun <reified R> Collection<KMutableProperty0<*>>.findKProperty(
    name: String,
    ignoreCase: Boolean = false
): Option<KMutableProperty0<R>> = CollectionUtils.Find.findKCallable(this, name, ignoreCase).filterIsInstance()

/** @see CollectionUtils.Find.findKCallable */
@JvmName("findKPropertyMutable1")
public inline fun <reified R> Collection<KMutableProperty1<*, *>>.findKProperty(
    name: String,
    ignoreCase: Boolean = false
): Option<KMutableProperty1<*, R>> = CollectionUtils.Find.findKCallable(this, name, ignoreCase).filterIsInstance()

/** @see CollectionUtils.Find.findKCallable */
@JvmName("findKPropertyMutable2")
public inline fun <reified R> Collection<KMutableProperty2<*, *, *>>.findKProperty(
    name: String,
    ignoreCase: Boolean = false
): Option<KMutableProperty2<*, *, R>> = CollectionUtils.Find.findKCallable(this, name, ignoreCase).filterIsInstance()

/** @see CollectionUtils.Find.findKCallable */
@JvmName("findKFunction")
public inline fun <reified R> Collection<KFunction<*>>.findKFunction(
    name: String,
    ignoreCase: Boolean = false
): Option<KFunction<R>> = CollectionUtils.Find.findKCallable(this, name, ignoreCase).filterIsInstance()

/** @see CollectionUtils.Find.findKCallable */
@JvmName("findKFunction0")
public inline fun <reified R> Collection<KFunction0<*>>.findKFunction(
    name: String,
    ignoreCase: Boolean = false
): Option<KFunction0<R>> = CollectionUtils.Find.findKCallable(this, name, ignoreCase).filterIsInstance()

/** @see CollectionUtils.Find.findKCallable */
@JvmName("findKFunction1")
public inline fun <reified R> Collection<KFunction1<*, *>>.findKFunction(
    name: String,
    ignoreCase: Boolean = false
): Option<KFunction1<Any, R>> = CollectionUtils.Find.findKCallable(this, name, ignoreCase).filterIsInstance()

/** @see CollectionUtils.Find.findKCallable */
@JvmName("findKFunction2")
public inline fun <reified R> Collection<KFunction2<*, *, *>>.findKFunction(
    name: String,
    ignoreCase: Boolean = false
): Option<KFunction2<*, *, R>> = CollectionUtils.Find.findKCallable(this, name, ignoreCase).filterIsInstance()

/** @see CollectionUtils.Mutate.clear */
public suspend inline fun <V> MutableCollection<V>.clear(
    crossinline onRemove: suspend V.() -> Unit
): Unit = CollectionUtils.Mutate.clear(this, onRemove)
