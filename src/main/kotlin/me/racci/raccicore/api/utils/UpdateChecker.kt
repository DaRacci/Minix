@file:Suppress("UNUSED")
package me.racci.raccicore.api.utils

import com.google.gson.JsonParser
import me.racci.raccicore.api.extensions.Console
import me.racci.raccicore.api.extensions.msg
import me.racci.raccicore.api.plugin.RacciPlugin
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.event.player.PlayerJoinEvent
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL

class UpdateChecker(
    private val plugin: RacciPlugin
) {

    init {
        plugin.update(plugin.spigotId)
    }

    private fun spiget(id: Int): String? = try {
        val base = "https://api.spiget.org/v2/resources/"
        val conn = URL("$base$id/versions?size=100").openConnection()
        val json = InputStreamReader(conn.inputStream).use { JsonParser.parseReader(it).asJsonArray }
        json.last().asJsonObject["name"].asString
    } catch (e: IOException) {
        null
    }

    private infix fun String.isNewerThan(v: String) = false.also {
        val s1 = split('.')
        val s2 = v.split('.')
        for (i in 0..s1.size.coerceAtLeast(s2.size)) {
            if (i !in s1.indices) return false
            if (i !in s2.indices) return true
            if (s1[i].toInt() > s2[i].toInt()) return true
            if (s1[i].toInt() < s2[i].toInt()) return false
        }
    }

    private fun RacciPlugin.update(
        id: Int,
        colour: TextColor = NamedTextColor.LIGHT_PURPLE,
        permission: String = "raccicore.update"
    ) {
        catch<Exception> {
            val new = spiget(id) ?: throw Exception("Could not retrieve latest version")
            val old = plugin.description.version
            if (!(new isNewerThan old)) return

            val url = "https://www.spigotmc.org/resources/$id"
            val msg = Component.text("An update is available for ${description.name} ($old -> $new): $url")
                .colorIfAbsent(colour)
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, url))

            Console.msg(msg)

            listen<PlayerJoinEvent> {
                if(it.player.hasPermission(permission)) {
                    it.player.sendMessage(msg)
                }
            }
        }
    }
}