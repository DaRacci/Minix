package dev.racci.minix.api.exceptions

import kotlin.reflect.KClass

class MissingAnnotationException(
    kClass: KClass<*>,
    annotation: KClass<Annotation>
) : RuntimeException("Missing annotation ${annotation.simpleName} on ${kClass.qualifiedName}")
