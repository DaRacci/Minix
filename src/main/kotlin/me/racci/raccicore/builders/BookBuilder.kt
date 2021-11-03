package me.racci.raccicore.builders

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import java.util.*

class BookBuilder internal constructor(itemStack: ItemStack) : BaseItemBuilder<BookBuilder>(itemStack) {

    var bItemStack = itemStack.clone()
        get() {field.itemMeta = bMeta;return field}

    private val bMeta get() = meta as BookMeta

    fun author(author: Component): BookBuilder {
        bMeta.author(author)
        return this
    }

    fun generation(generation: BookMeta.Generation): BookBuilder {
        bMeta.generation = generation
        return this
    }

    fun page(vararg pages: Component): BookBuilder {
        bMeta.addPages(*pages)
        return this
    }

    fun page(page: Int, data: Component): BookBuilder {
        bMeta.page(page, data)
        return this
    }

    fun title(title: Component): BookBuilder {
        bMeta.title(title)
        return this
    }

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