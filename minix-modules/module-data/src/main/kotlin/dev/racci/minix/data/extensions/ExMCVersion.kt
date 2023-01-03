package dev.racci.minix.data.extensions

import dev.racci.minix.api.utils.minecraft.MCVersion

// Is this a dumb idea? Probably. But it's a dumb idea that I want to do.
private val supportLevel by lazy {
    fun hasClass(string: String) = runCatching { Class.forName(string) }.isSuccess

    if (hasClass("dev.racci.TentaclesConfig")) {
        4
    } else if (hasClass("org.purpurmc.purpur.PurpurConfig")) {
        3
    } else if (hasClass("com.destroystokyo.paper.PaperConfig") || hasClass("io.papermc.paper.configuration.Configuration")) {
        2
    } else if (hasClass("org.spigotmc.SpigotConfig")) {
        1
    } else 0
}

public val MCVersion.Companion.supportsTentacles: Boolean
    get() = supportLevel >= 4

public val MCVersion.Companion.supportsPurpur: Boolean
    get() = supportLevel >= 3

public val MCVersion.Companion.supportsPaper: Boolean
    get() = supportLevel >= 2
