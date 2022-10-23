package dev.racci.minix.api.paper.builders

import net.kyori.adventure.text.Component
import org.bukkit.inventory.meta.BookMeta

public interface BookBuilder : BaseItemBuilder<BookBuilder, BookMeta> {

    /**
     * Get or set the title of the book.
     */
    public var title: Component?

    /**
     * Get or set the description of the book.
     */
    public var author: Component?

    /**
     * Get or set the [BookMeta.Generation] of the book.
     */
    public var generation: BookMeta.Generation?

    /**
     * Add the pages to the book.
     */
    public fun addPage(vararg pages: Component): BookBuilder

    /**
     * Sets the page [Int] of the [Pair] to the component.
     */
    public fun setPages(vararg pages: Pair<Int, Component>): BookBuilder
}
