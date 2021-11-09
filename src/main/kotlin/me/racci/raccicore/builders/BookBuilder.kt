package me.racci.raccicore.builders

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import org.bukkit.inventory.meta.MapMeta
import java.util.EnumSet

class BookBuilder internal constructor(itemStack: ItemStack) : BaseItemBuilder<BookBuilder>(itemStack) {

    var author: Component
        get() = (meta as BookMeta).author()
        set(author) {(meta as BookMeta).author(author)}

    var generation: BookMeta.Generation?
        get() = (meta as BookMeta).generation
        set(generation) {(meta as BookMeta).generation = generation}

    fun page(vararg pages: Component): BookBuilder {
        (meta as BookMeta).addPages(*pages)
        return this
    }

    var page: Pair<Int, Component>
        get() = throw UnsupportedOperationException()
        set(pair) {(meta as BookMeta).page(pair.first, pair.second)}

    fun page(page: Int, data: Component): BookBuilder {
        (meta as BookMeta).page(page, data)
        return this
    }

    var title: Component
        get() = (meta as BookMeta).title()
        set(title) {(meta as BookMeta).title(title)}

    companion object {
        private val BOOKS: EnumSet<Material> = EnumSet.of(Material.WRITABLE_BOOK, Material.WRITTEN_BOOK)
    }

    init {
        if (!BOOKS.contains(itemStack.type)) {
            throw UnsupportedOperationException("BookBuilder requires the material to be a WRITABLE_BOOK/WRITTEN_BOOK!")
        }
    }
}