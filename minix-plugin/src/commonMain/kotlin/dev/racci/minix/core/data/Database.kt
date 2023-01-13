package dev.racci.minix.core.data

import org.bukkit.plugin.Plugin
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.koin.core.component.KoinComponent

internal object PluginData : IdTable<String>("plugin") {

    override val id: Column<EntityID<String>> = text("name").entityId()
    var newVersion: Column<String> = text("new_version")
    var oldVersion: Column<String> = text("old_version")
}

internal class DataHolder(plugin: EntityID<String>) : Entity<String>(plugin) {
    var newVersion: String by PluginData.newVersion
    var oldVersion: String by PluginData.oldVersion

    internal companion object : EntityClass<String, DataHolder>(PluginData), KoinComponent {

        fun getOrNull(id: String): DataHolder? = find { PluginData.id eq id }.firstOrNull()

        fun getOrNull(plugin: Plugin): DataHolder? = getOrNull(plugin.name)

        operator fun get(plugin: Plugin): DataHolder = get(plugin.name)
    }
}
