import strikt.api.Assertion

inline fun <reified T, reified R> Assertion.Builder<T>.expect(
    description: String = "",
    expected: R?,
    crossinline block: (T) -> R?
): Assertion.Builder<T> = this.assertThat(description) {
    block(it) == expected
}

inline fun <reified T, reified R : Throwable> Assertion.Builder<*>.expectThrows(
    description: String = "",
    crossinline block: (T) -> Unit
): Assertion.Builder<T> = assert(description) {
    try {
        block(it as T)
        pass()
    } catch (e: Throwable) {
        if (e is R) {
            pass()
        } else {
            fail(R::class, "Expected exception of type ${R::class.simpleName} but got ${e.javaClass.simpleName}", e)
        }
    }
} as Assertion.Builder<T>
