package me.racci.raccicore.events

import org.bukkit.entity.Entity

interface IComboEntityEvent: IComboEvent {

    val entity: Entity

}