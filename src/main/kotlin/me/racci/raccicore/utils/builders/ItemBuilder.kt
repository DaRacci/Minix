//package me.racci.raccicore.utils.builders
//
//import com.sun.tools.javac.jvm.ByteCodes.lor
//import me.racci.raccicore.utils.strings.colour
//import net.kyori.adventure.text.Component
//import net.kyori.adventure.text.format.TextDecoration
//import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
//import net.minecraft.world.item.enchantment.Enchantment
//import org.bukkit.Bukkit
//import org.bukkit.Material
//import org.bukkit.inventory.ItemFlag
//import org.bukkit.inventory.ItemStack
//import org.bukkit.inventory.meta.ItemMeta
//import org.bukkit.persistence.PersistentDataContainer
//import java.util.function.Consumer
//
//@DslMarker
//@Retention(AnnotationRetention.BINARY)
//annotation class ItemBuilderDSLMarker
//
//@ItemBuilderDSLMarker
//inline fun itemBuilder(
//    material: Material,
//    block: ItemDSLBuilder.() -> Unit
//) = item(ItemStack(material), block)
//
//@ItemBuilderDSLMarker
//inline fun item(
//    itemStack: ItemStack,
//    block: ItemDSLBuilder.() -> Unit
//) : ItemDSL = ItemDSLBuilder(itemStack).apply(block)
////
////
////
////interface ItemDSL {
////
////    fun removeNbt(key: String)
////
////    fun build()
////
////    fun getItemStack()
////
////    fun setItemStack()
////
////    fun getMeta()
////
////    fun setMeta(meta: ItemMeta)
////
////}
//
//fun test() {
//    itemBuilder(Material.CLOCK) {
//
//        build()
//    }
//
//}
//
//class ItemDSLBuilder(itemStack: ItemStack) {
//
//    var itemStack: ItemStack
//    var meta: ItemMeta
//
//    init {
//        this.itemStack = itemStack
//        this.meta = itemStack.itemMeta ?: Bukkit.getItemFactory().getItemMeta(itemStack.type)
//    }
//
//    var name: Component
//        get() = meta.displayName() ?: Component.empty()
//        set(name) = meta.displayName(name)
//
//    var amount: Int
//        get() = itemStack.amount
//        set(amount) {
//            itemStack.amount = amount
//        }
//
//    var material: Material
//        get() = itemStack.type
//        set(material) {
//            itemStack.type = material
//        }
//
//    var lore: Array<Component>
//        get() = meta.lore()?.toTypedArray() ?: emptyArray()
//        set(lore) {
//            meta.lore(lore.asList())
//        }
//
//    fun lore(vararg lore: Component) {
//        lore(lore.asList())
//    }
//    fun lore(lore: List<Component>) {
//        meta.lore(lore)
//    }
//    fun lore(lore: Consumer<List<Component>>) {
//        var metaLore = meta.lore()
//        if(metaLore == null) metaLore = ArrayList()
//        lore.accept(metaLore)
//    }
//
//    fun enchant(enchantment: Enchantment, level: Int, ignoreRestrictions: Boolean) {
//        TODO("Not yet implemented")
//    }
//
//    fun disenchant(enchantment: Enchantment) {
//        TODO("Not yet implemented")
//    }
//
//    fun flags(vararg flags: ItemFlag) {
//        TODO("Not yet implemented")
//    }
//
//    fun unbreakable(unbreakable: Boolean) {
//        TODO("Not yet implemented")
//    }
//
//    fun glow(glow: Boolean) {
//        TODO("Not yet implemented")
//    }
//
//    fun pdc(consumer: Consumer<PersistentDataContainer>) {
//        TODO("Not yet implemented")
//    }
//
//    fun model(modelData: Int) {
//        TODO("Not yet implemented")
//    }
//
//    fun nbt(key: String, value: String) {
//        TODO("Not yet implemented")
//    }
//
//    fun nbt(key: String, value: Boolean) {
//        TODO("Not yet implemented")
//    }
//
//    fun removeNbt(key: String) {
//        TODO("Not yet implemented")
//    }
//
//    fun build() {
//        TODO("Not yet implemented")
//    }
//
//}