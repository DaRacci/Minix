package dev.racci.minix.api.autoscanner

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs

class ScannerTest : FunSpec({
    test("Cache matches new instances with same input.") {
        val scanner1 = Scanner.of("dev.racci.minix")
        val scanner2 = Scanner.of("dev.racci.minix")

        scanner1.getResult() shouldBeSameInstanceAs scanner2.getResult()
    }

    test("Cache does not match new instances with different input.") {
        val scanner1 = Scanner.of("dev.racci.minix")
        val scanner2 = Scanner.of("dev.racci.minix") { ignoreParentClassLoaders() }

        scanner1.getResult() shouldNotBeSameInstanceAs scanner2.getResult()
    }

    test("#withAnnotation returns only the annotated classes.") {
        Scanner.global().withAnnotation<SinceKotlin>().forEach {
            it.hasAnnotation(SinceKotlin::class.java) shouldBe true
        }
    }

    test("#withSuperclass returns only the classes with the superclass.") {
        Scanner.global().withSuperclass<ClassLoader>().forEach {
            it.isStandardClass shouldBe true
            it.extendsSuperclass(ClassLoader::class.java) shouldBe true
        }
    }

    test("#withInterface returns only the classes with the interface.") {
        Scanner.global().withInterface<Runnable>().forEach {
            it.implementsInterface(Runnable::class.java) shouldBe true
        }
    }
})
