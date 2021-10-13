package me.racci.raccicore.utils

import com.google.gson.JsonParser
import me.racci.raccicore.RacciPlugin
import me.racci.raccicore.skedule.schedule
import me.racci.raccicore.skedule.skeduleAsync
import me.racci.raccicore.utils.strings.textOf
import net.kyori.adventure.text.event.ClickEvent
import org.bukkit.ChatColor
import org.bukkit.event.player.PlayerJoinEvent
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL

class UpdateChecker(plugin: RacciPlugin, id: Int) : PluginDependent<RacciPlugin>(plugin) {

    init {
        plugin.update(id)
    }

    private fun spiget(id: Int): String? = try {
        val base = "https://api.spiget.org/v2/resources/"
        val conn = URL("$base$id/versions?size=100").openConnection()
        val json = InputStreamReader(conn.inputStream).let { JsonParser().parse(it).asJsonArray }
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
        color: ChatColor = ChatColor.LIGHT_PURPLE,
        permission: String = "raccicore.update"
    ) = catch<Exception> {
        skeduleAsync(plugin) {
            val new = spiget(id)
                ?: throw Exception("Could not retrieve latest version")

            val old = description.version
            if (!(new isNewerThan old)) return@skeduleAsync

            val url = "https://www.spigotmc.org/resources/$id"
            val msg = "${color}An update is available for ${description.name} ($old -> $new): $url"

            schedule { console.sendMessage(msg) }
            listen<PlayerJoinEvent> {
                if (it.player.hasPermission(permission))
                    try {
                        val text = textOf(msg) { ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, url) }
                        it.player.sendMessage(text)
                    } catch (ex: Error) {
                        it.player.sendMessage(msg)
                    }
            }
        }
    }
}