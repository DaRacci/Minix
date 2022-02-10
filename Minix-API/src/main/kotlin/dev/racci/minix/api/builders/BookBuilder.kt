package dev.racci.minix.api.builders

import net.kyori.adventure.text.Component
import org.bukkit.inventory.meta.BookMeta

interface BookBuilder : BaseItemBuilder<BookBuilder, BookMeta> {

    /**
     * Get or set the title of the book.
     */
    var title: Component?

    /**
     * Get or set the description of the book.
     */
    var author: Component?

    /**
     * Get or set the [BookMeta.Generation] of the book.
     */
    var generation: BookMeta.Generation?

    /**
     * Add the pages to the book.
     */
    fun addPage(vararg pages: Component): BookBuilder

    /**
     * Sets the page [Int] of the [Pair] to the component.
     */
    fun setPages(vararg pages: Pair<Int, Component>): BookBuilder
}
