package me.racci.raccicore.core

import me.racci.raccicore.core.managers.BungeeCordManager
import me.racci.raccicore.core.managers.CommandManager
import me.racci.raccicore.core.managers.PlayerManager
import kotlin.properties.Delegates

internal object Provider {

    var playerManager by Delegates.notNull<PlayerManager>()
    var commandManager by Delegates.notNull<CommandManager>()
    var bungeeCordManager by Delegates.notNull<BungeeCordManager>()

}