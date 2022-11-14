package dev.racci.minix.core.builders

import dev.racci.minix.api.paper.builders.BookBuilder
import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta

public class BookBuilderImpl internal constructor(
    itemStack: ItemStack
) : BaseItemBuilderImpl<BookBuilder, BookMeta>(itemStack), BookBuilder {

    override var title: Component?
        get() = meta.title()
        set(title) {
            meta.title(title)
        }

    override var author: Component?
        get() = if (meta.hasAuthor()) meta.author() else null
        set(author) {
            meta.author(author)
        }

    override var generation: BookMeta.Generation?
        get() = meta.generation
        set(generation) {
            meta.generation = generation
        }

    override fun addPage(vararg pages: Component): BookBuilder {
        meta.addPages(*pages)
        return this
    }

    override fun setPages(vararg pages: Pair<Int, Component>): BookBuilder {
        pages.forEach { meta.page(it.first, it.second) }
        return this
    }
}
