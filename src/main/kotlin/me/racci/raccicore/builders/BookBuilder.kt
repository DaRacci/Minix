package me.racci.raccicore.builders

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import java.util.EnumSet

class BookBuilder internal constructor(itemStack: ItemStack) : BaseItemBuilder<BookBuilder>(itemStack) {

    var bItemStack = itemStack.clone()
        get() {field.itemMeta = bMeta;return field}

    private val bMeta = meta.clone() as BookMeta

    var author: Component
        get() = bMeta.author()
        set(author) {bMeta.author(author)}

    var generation: BookMeta.Generation?
        get() = bMeta.generation
        set(generation) {bMeta.generation = generation}

    fun page(vararg pages: Component): BookBuilder {
        bMeta.addPages(*pages)
        return this
    }

    var page: Pair<Int, Component>
        get() = throw UnsupportedOperationException()
        set(pair) {bMeta.page(pair.first, pair.second)}

    fun page(page: Int, data: Component): BookBuilder {
        bMeta.page(page, data)
        return this
    }

    var title: Component
        get() = bMeta.title()
        set(title) {bMeta.title(title)}

    override fun build(): ItemStack {
        bItemStack.itemMeta = bMeta
        return bItemStack
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