package dev.racci.minix.api.exceptions

import kotlin.reflect.KClass

public class MissingAnnotationException(
    kClass: KClass<*>,
    annotation: KClass<out Annotation>
) : RuntimeException("Missing annotation ${annotation.simpleName} on ${kClass.qualifiedName}")
