package dev.racci.minix.api.services

import com.zaxxer.hikari.HikariDataSource
import dev.racci.minix.api.extension.ExtensionSkeleton
import dev.racci.minix.api.extensions.deleteProperty
import dev.racci.minix.api.extensions.getProperty
import dev.racci.minix.api.extensions.setProperty
import dev.racci.minix.api.logger.MinixLogger
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.getKoin
import kotlinx.coroutines.Deferred
import org.apiguardian.api.API
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlLogger
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.StatementContext
import org.jetbrains.exposed.sql.statements.expandArgs
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import java.nio.file.Path
import javax.sql.DataSource

/**
 * Base class for managing your plugin's data.
 * Automatically creates a database connection and provides a [Database] instance.
 */
@API(status = API.Status.EXPERIMENTAL, since = "4.2.0")
public interface StorageService<P : MinixPlugin> : ExtensionSkeleton<P> {
    public val managedTable: Table

    @ApiStatus.NonExtendable
    public override suspend fun handleLoad() {
        this.createDatabaseConfig()
        this.setProperty(PROPERTY_DATABASE, Database.connect(this.getProperty<DataSource>(PROPERTY_DATA_SOURCE)!!, databaseConfig = databaseConfig))

        withDatabase {
            SchemaUtils.create(managedTable)
            SchemaUtils.addMissingColumnsStatements(managedTable)
        }
    }

    @ApiStatus.NonExtendable
    public override suspend fun handleReload() {
        // TODO -> Reload database settings and re-initialise
    }

    @ApiStatus.NonExtendable
    public override suspend fun handlePostUnload() {
        this.getProperty<HikariDataSource>(PROPERTY_DATA_SOURCE)?.close()
        this.deleteProperty(PROPERTY_DATA_SOURCE)
        this.deleteProperty(PROPERTY_DATABASE)
    }

    public suspend fun getDatabase(): Database? {
        return this.getProperty(PROPERTY_DATABASE)
    }

    public fun getStorageDirectory(): Path = plugin.dataFolder

    // TODO -> Configurable in MinixConfig file
    public fun getDataSourceProperties(): Map<String, Any> = mapOf(
        "cachePrepStmts" to true,
        "prepStmtCacheSize" to 250,
        "prepStmtCacheSqlLimit" to 2048
    )

    // TODO -> MariaDB support
    // TODO -> HyperSQL support
    // TODO -> Support changing through MinixConfig file
    private fun createDatabaseConfig(): DataSource {
        return with(HikariDataSource()) {
            this.jdbcUrl = "jdbc:sqlite:${getStorageDirectory()}/database.db"

            this@StorageService.getDataSourceProperties().forEach { (key, value) ->
                this.addDataSourceProperty(key, value)
            }

            setProperty(PROPERTY_DATA_SOURCE, this)
        }
    }

    public suspend fun <T> withDatabase(
        block: suspend Transaction.() -> T
    ): T = newSuspendedTransaction(this.dispatcher.get(), this.getDatabase()) {
        block()
    }

    public suspend fun <T> withDatabaseAsync(
        block: suspend Transaction.() -> T
    ): Deferred<T> = suspendedTransactionAsync(this.dispatcher.get(), this.getDatabase()) {
        block()
    }

    private companion object {
        const val PROPERTY_DATABASE = "database"
        const val PROPERTY_DATA_SOURCE = "datasource"

        val databaseConfig = DatabaseConfig {
            this.sqlLogger = object : SqlLogger {
                override fun log(
                    context: StatementContext,
                    transaction: Transaction
                ) = getKoin().get<MinixLogger>().debug { "Executing SQL: ${context.expandArgs(transaction)}" }
            }
        }
    }
}
