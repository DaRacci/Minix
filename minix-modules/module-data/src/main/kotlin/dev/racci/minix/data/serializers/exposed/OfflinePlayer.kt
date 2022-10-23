package dev.racci.minix.data.serializers.exposed

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.Column
import java.util.UUID
import kotlin.reflect.KProperty

public fun Entity<*>.offlinePlayer(column: Column<UUID>): OfflinePlayerExposedDelegate = OfflinePlayerExposedDelegate(column)
public fun Entity<*>.nullableOfflinePlayer(column: Column<UUID?>): OfflinePlayerExposedDelegateNullable = OfflinePlayerExposedDelegateNullable(column)

public class OfflinePlayerExposedDelegate(
    public val column: Column<UUID>
) : ExposedDelegate<OfflinePlayer> {

    override operator fun <ID : Comparable<ID>> getValue(
        entity: Entity<ID>,
        desc: KProperty<*>
    ): OfflinePlayer {
        val uuid = entity.run { column.getValue(this, desc) }
        return Bukkit.getOfflinePlayer(uuid)
    }

    override operator fun <ID : Comparable<ID>> setValue(
        entity: Entity<ID>,
        desc: KProperty<*>,
        value: OfflinePlayer
    ) {
        entity.apply { column.setValue(this, desc, value.uniqueId) }
    }
}

public class OfflinePlayerExposedDelegateNullable(
    public val column: Column<UUID?>
) : ExposedDelegate<OfflinePlayer?> {

    override operator fun <ID : Comparable<ID>> getValue(
        entity: Entity<ID>,
        desc: KProperty<*>
    ): OfflinePlayer? {
        val uuid = entity.run { column.getValue(this, desc) }
        return uuid?.let { Bukkit.getOfflinePlayer(it) }
    }

    override operator fun <ID : Comparable<ID>> setValue(
        entity: Entity<ID>,
        desc: KProperty<*>,
        value: OfflinePlayer?
    ) {
        entity.apply { column.setValue(this, desc, value?.uniqueId) }
    }
}
