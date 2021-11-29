@file:Suppress("UNUSED")
package me.racci.raccicore.api.builders

import com.google.common.collect.Multimap
import me.racci.raccicore.api.extensions.gameProfileConstructor
import me.racci.raccicore.api.extensions.getPropertiesMethod
import me.racci.raccicore.api.extensions.propertyConstructor
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

class HeadBuilder internal constructor(itemStack: ItemStack): BaseItemBuilder<HeadBuilder>(itemStack) {

    var texture: String
        get() = throw UnsupportedOperationException("Please don't make me implement this")
        set(texture) {
            val profile = gameProfileConstructor.newInstance(UUID.fromString("38dff22c-c0ec-40b8-bd11-b4376e9a20a6"), null as String?)
            val properties = getPropertiesMethod.invoke(profile) as Multimap<Any, Any>
            properties.put("textures", propertyConstructor.newInstance("textures", texture))
            val profileField = meta.javaClass.getDeclaredField("profile").apply {isAccessible = true}
            profileField.set(meta, profile)
        }

    var owner: OfflinePlayer?
        get() = (meta as SkullMeta).owningPlayer
        set(player) {(meta as SkullMeta).owningPlayer = player}

    init {
        if (itemStack.type != Material.PLAYER_HEAD) {
            throw UnsupportedClassVersionError("SkullBuilder requires the material to be a PLAYER_HEAD!")
        }
    }
}