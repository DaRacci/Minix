package me.racci.raccilib.utils.items.builders

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import java.util.*

/**
 * Item builder for [Material.WRITTEN_BOOK] and [Material.WRITTEN_BOOK] only
 *
 * @author GabyTM [https://github.com/iGabyTM](https://github.com/iGabyTM)
 * @since 3.0.1
 */
class BookBuilder internal constructor(itemStack: ItemStack) : BaseItemBuilder<BookBuilder>(itemStack) {
    /**
     * Sets the author of the book. Removes author when given null.
     *
     * @param author the author to set
     * @return [BookBuilder]
     * @since 3.0.1
     */
    fun author(author: Component?): BookBuilder {
        val bookMeta = getMeta() as BookMeta
        if (author == null) {
            bookMeta.author = null
            setMeta(bookMeta)
            return this
        }
        setMeta(bookMeta.author(author))
        return this
    }

    /**
     * Sets the generation of the book. Removes generation when given null.
     *
     * @param generation the generation to set
     * @return [BookBuilder]
     * @since 3.0.1
     */
    fun generation(generation: BookMeta.Generation?): BookBuilder {
        val bookMeta = getMeta() as BookMeta
        bookMeta.generation = generation
        setMeta(bookMeta)
        return this
    }

    /**
     * Adds new pages to the end of the book. Up to a maximum of 50 pages with
     * 256 characters per page.
     *
     * @param pages list of pages
     * @return [BookBuilder]
     * @since 3.0.1
     */
    fun page(vararg pages: Component): BookBuilder {
        return page(listOf(*pages))
    }

    /**
     * Adds new pages to the end of the book. Up to a maximum of 50 pages with
     * 256 characters per page.
     *
     * @param pages list of pages
     * @return [BookBuilder]
     * @since 3.0.1
     */
    private fun page(pages: List<Component>): BookBuilder {
        val bookMeta = getMeta() as BookMeta
        bookMeta.addPages(*pages.toTypedArray())
        setMeta(bookMeta)
        return this
    }

    /**
     * Sets the specified page in the book. Pages of the book must be
     * contiguous.
     *
     *
     * The data can be up to 256 characters in length, additional characters
     * are truncated.
     *
     *
     * Pages are 1-indexed.
     *
     * @param page the page number to set, in range [1, [BookMeta.getPageCount]]
     * @param data the data to set for that page
     * @return [BookBuilder]
     * @since 3.0.1
     */
    fun page(page: Int, data: Component): BookBuilder {
        val bookMeta = getMeta() as BookMeta
        bookMeta.page(page, data)
        setMeta(bookMeta)
        return this
    }

    /**
     * Sets the title of the book.
     *
     *
     * Limited to 32 characters. Removes title when given null.
     *
     * @param title the title to set
     * @return [BookBuilder]
     * @since 3.0.1
     */
    fun title(title: Component?): BookBuilder {
        val bookMeta = getMeta() as BookMeta
        if (title == null) {
            bookMeta.title = null
            setMeta(bookMeta)
            return this
        }
        setMeta(bookMeta.title(title))
        return this
    }

    companion object {
        private val BOOKS: EnumSet<Material> = EnumSet.of(Material.WRITABLE_BOOK, Material.WRITTEN_BOOK)
    }

    init {
        if (!BOOKS.contains(itemStack.type)) {
            throw UnsupportedOperationException("BookBuilder requires the material to be a WRITABLE_BOOK/WRITTEN_BOOK!")
        }
    }
}