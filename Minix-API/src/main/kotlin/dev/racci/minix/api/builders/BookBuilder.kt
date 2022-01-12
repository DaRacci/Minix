@file:Suppress("UNUSED")
package dev.racci.minix.api.builders

import dev.racci.minix.api.annotations.MinixDsl
import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta

/**
 * Book Builder Util.
 */
@MinixDsl
class BookBuilder internal constructor(
    itemStack: ItemStack
): BaseItemBuilder<BookBuilder, BookMeta>(itemStack) {

    /**
     * Get or set the title of the book.
     */
    var title: Component?
        get() = meta.title()
        set(title) { meta.title(title) }

    /**
     * Get or set the description of the book.
     */
    var author: Component?
        get() = if(meta.hasAuthor()) meta.author() else null
        set(author) { meta.author(author) }

    /**
     * Get or set the [BookMeta.Generation] of the book.
     */
    var generation: BookMeta.Generation?
        get() = meta.generation
        set(generation) { meta.generation = generation }

    /**
     * Add the pages to the book.
     */
    fun addPage(vararg pages: Component): BookBuilder {
        meta.addPages(*pages)
        return this
    }

    /**
     * Sets the page [Int] of the [Pair] to the component.
     */
    fun setPages(vararg pages: Pair<Int, Component>): BookBuilder {
        pages.forEach { meta.page(it.first, it.second) }
        return this
    }
}
