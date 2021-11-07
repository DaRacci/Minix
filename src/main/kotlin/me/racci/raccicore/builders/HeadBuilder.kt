package me.racci.raccicore.builders

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import me.racci.raccicore.utils.catch
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
            var field: Field? = null
            catch<NoSuchFieldException>({it.printStackTrace()}) {
                val skullMeta = ItemStack(Material.PLAYER_HEAD).itemMeta as SkullMeta
                field = skullMeta.javaClass.getDeclaredField("profile")
                field?.isAccessible = true
            }
            PROFILE_FIELD = field
        }
    }

    private var hMeta = meta.clone() as SkullMeta
    private var hItemStack = itemStack.clone()
        get() {field.itemMeta = hMeta;return field}

    var texture: String
        get() = throw UnsupportedOperationException("Please don't make me implement this")
        set(texture) {
            if (PROFILE_FIELD == null) return
            val skullMeta = hMeta
            val profile = GameProfile(UUID.fromString("38dff22c-c0ec-40b8-bd11-b4376e9a20a6"), null)
            profile.properties.put("textures", Property("textures", texture))
            catch<Exception> {
                PROFILE_FIELD!![hMeta] = profile
            }
            meta = skullMeta
        }

    var owner: OfflinePlayer?
        get() = hMeta.owningPlayer
        set(player) {hMeta.owningPlayer = player}

    override fun build(): ItemStack {
        hItemStack.itemMeta = hMeta
        return hItemStack
    }

    init {
        if (itemStack.type != Material.PLAYER_HEAD) {
            throw UnsupportedClassVersionError("SkullBuilder requires the material to be a PLAYER_HEAD!")
        }
    }
}