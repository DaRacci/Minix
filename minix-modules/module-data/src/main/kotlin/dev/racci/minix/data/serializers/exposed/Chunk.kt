package dev.racci.minix.data.serializers.exposed

import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.Column
import kotlin.reflect.KProperty

public fun Entity<*>.chunk(column: Column<String>): ChunkExposedDelegate = ChunkExposedDelegate(column)
public fun Entity<*>.nullableChunk(column: Column<String?>): ChunkExposedDelegateNullable = ChunkExposedDelegateNullable(column)

public fun Entity<*>.chunk(
    worldColumn: Column<String>,
    xColumn: Column<Int>,
    zColumn: Column<Int>
): ChunkMultiColumnExposedDelegate = ChunkMultiColumnExposedDelegate(worldColumn, xColumn, zColumn)

public fun Entity<*>.chunk(
    worldColumn: Column<String?>,
    xColumn: Column<Int?>,
    zColumn: Column<Int?>
): ChunkMultiColumnExposedDelegateNullable = ChunkMultiColumnExposedDelegateNullable(worldColumn, xColumn, zColumn)

public class ChunkExposedDelegate(
    public val column: Column<String>
) : ExposedDelegate<Chunk> {

    override operator fun <ID : Comparable<ID>> getValue(
        entity: Entity<ID>,
        desc: KProperty<*>
    ): Chunk {
        val data = entity.run { column.getValue(this, desc) }
        val slices = data.split(";")
        return Bukkit.getWorld(slices[0])!!.getChunkAt(
            slices[1].toInt(),
            slices[2].toInt()
        )
    }

    override operator fun <ID : Comparable<ID>> setValue(
        entity: Entity<ID>,
        desc: KProperty<*>,
        value: Chunk
    ) {
        val parsed = value.run { "${world.name};$x;$z" }
        entity.apply { column.setValue(this, desc, parsed) }
    }
}

public class ChunkExposedDelegateNullable(
    public val column: Column<String?>
) : ExposedDelegate<Chunk?> {

    override operator fun <ID : Comparable<ID>> getValue(
        entity: Entity<ID>,
        desc: KProperty<*>
    ): Chunk? {
        val data = entity.run { column.getValue(this, desc) }
        val slices = data?.split(";")
        return slices?.let {
            Bukkit.getWorld(it[0])?.getChunkAt(
                it[1].toInt(),
                it[2].toInt()
            )
        }
    }

    override operator fun <ID : Comparable<ID>> setValue(
        entity: Entity<ID>,
        desc: KProperty<*>,
        value: Chunk?
    ) {
        val parsed = value?.run { "${world.name};$x;$z" }
        entity.apply { column.setValue(this, desc, parsed) }
    }
}

public class ChunkMultiColumnExposedDelegate(
    public val worldColumn: Column<String>,
    public val xColumn: Column<Int>,
    public val zColumn: Column<Int>
) : ExposedDelegate<Chunk> {

    override operator fun <ID : Comparable<ID>> getValue(
        entity: Entity<ID>,
        desc: KProperty<*>
    ): Chunk {
        val worldName = entity.run { worldColumn.getValue(this, desc) }
        val x = entity.run { xColumn.getValue(this, desc) }
        val z = entity.run { zColumn.getValue(this, desc) }

        return Bukkit.getWorld(worldName)!!.getChunkAt(x, z)
    }

    override operator fun <ID : Comparable<ID>> setValue(
        entity: Entity<ID>,
        desc: KProperty<*>,
        value: Chunk
    ) {
        entity.apply {
            value.apply {
                worldColumn.setValue(entity, desc, world.name)
                xColumn.setValue(entity, desc, x)
                zColumn.setValue(entity, desc, z)
            }
        }
    }
}

public class ChunkMultiColumnExposedDelegateNullable(
    public val worldColumn: Column<String?>,
    public val xColumn: Column<Int?>,
    public val zColumn: Column<Int?>
) : ExposedDelegate<Chunk?> {

    override operator fun <ID : Comparable<ID>> getValue(
        entity: Entity<ID>,
        desc: KProperty<*>
    ): Chunk? {
        val worldName = entity.run { worldColumn.getValue(this, desc) }
        val x = entity.run { xColumn.getValue(this, desc) }
        val z = entity.run { zColumn.getValue(this, desc) }

        return if (
            worldName != null &&
            x != null && z != null
        ) Bukkit.getWorld(worldName)?.getChunkAt(
            x,
            z
        ) else null
    }

    override operator fun <ID : Comparable<ID>> setValue(
        entity: Entity<ID>,
        desc: KProperty<*>,
        value: Chunk?
    ) {
        entity.apply {
            worldColumn.setValue(entity, desc, value?.world?.name)
            xColumn.setValue(entity, desc, value?.x)
            zColumn.setValue(entity, desc, value?.z)
        }
    }
}
