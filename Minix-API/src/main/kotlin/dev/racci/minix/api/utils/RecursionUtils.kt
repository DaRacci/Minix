package dev.racci.minix.api.utils

object RecursionUtils : UtilObject by UtilObject {

    /**
     * Recursive finder
     *
     * @param start The extension to start from
     * @param curDepth The current depth
     * @param maxDepth The maximum depth
     * @param finder The finder function
     * @param filter The filter function
     * @param current The current list of results
     * @param T The type of the result
     */
    fun <T> recursiveFinder(
        start: T,
        curDepth: Int = 0,
        maxDepth: Int = 9,
        finder: T.() -> Iterable<T>,
        filter: T.() -> Boolean,
        current: HashSet<T> = HashSet()
    ): HashSet<T> {
        if (curDepth > maxDepth) return hashSetOf()

        for (item in start.finder()) {
            if (item.filter()) continue
            if (item in current) continue

            current += item
            current += recursiveFinder(item, curDepth + 1, maxDepth, finder, filter, current)
        }

        return current
    }
}
