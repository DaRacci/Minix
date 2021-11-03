//package me.racci.raccicore.hooks
//
//import me.clip.placeholderapi.expansion.PlaceholderExpansion
//import me.racci.raccicore.RacciPlugin
//import org.bukkit.OfflinePlayer
//import java.util.function.BiFunction
//
//class PlaceholderAPIHook(private val hooker: RacciPlugin) : PlaceholderExpansion() {
//    override fun getIdentifier(): String    = hooker.description.name
//    override fun getAuthor()    : String    = hooker.description.authors.joinToString(", ")
//    override fun getVersion()   : String    = hooker.description.version
//    override fun persist()      : Boolean   = true
//
//    val placeholders = HashMap<String, BiFunction<String, OfflinePlayer, String>>()
//
//    override fun onRequest(player: OfflinePlayer, params: String): String? =
//        placeholders[params.lowercase()]?.apply(params, player)
//
//}