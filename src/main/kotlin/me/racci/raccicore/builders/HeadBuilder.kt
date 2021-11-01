package me.racci.raccicore.builders

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.lang.reflect.Field
import java.util.*

class HeadBuilder internal constructor(itemStack: ItemStack) : BaseItemBuilder<HeadBuilder>(itemStack) {

    companion object {
        private var PROFILE_FIELD: Field? = null

        init {
            var field: Field?
            try {
                val skullMeta = ItemStack(Material.PLAYER_HEAD).itemMeta as SkullMeta
                field = skullMeta.javaClass.getDeclaredField("profile")
                field.isAccessible = true
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
                field = null
            }
            PROFILE_FIELD = field
        }
    }

    override var itemStack = itemStack.clone()
        get() {field.itemMeta = sMeta;return field}

    private val sMeta get() = meta as SkullMeta

    fun texture(texture: String): HeadBuilder {
        if (itemStack.type != Material.PLAYER_HEAD) return this
        if (PROFILE_FIELD == null) {
            return this
        }
        val skullMeta = sMeta
        val profile = GameProfile(UUID.fromString("38dff22c-c0ec-40b8-bd11-b4376e9a20a6"), null)
        profile.properties.put("textures", Property("textures", texture))
        try {
            PROFILE_FIELD!![sMeta] = profile
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        } catch (ex: IllegalAccessException) {
            ex.printStackTrace()
        }
        meta = skullMeta
        return this
    }

    fun owner(player: OfflinePlayer): HeadBuilder {
        if (itemStack.type != Material.PLAYER_HEAD) return this
        val t = sMeta
        t.owningPlayer = player
        return this
    }

    init {
        if (itemStack.type != Material.PLAYER_HEAD) {
            throw UnsupportedClassVersionError("SkullBuilder requires the material to be a PLAYER_HEAD!")
        }
    }
}