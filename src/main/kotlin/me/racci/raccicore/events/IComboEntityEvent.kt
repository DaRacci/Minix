package me.racci.raccicore.events

import org.bukkit.entity.LivingEntity

interface IComboEntityEvent: IComboEvent {

    val entity: LivingEntity

}