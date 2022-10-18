package dev.racci.minix.api.services

import com.zaxxer.hikari.HikariDataSource
import dev.racci.minix.api.extension.ExtensionSkeleton
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.logger.MinixLogger
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
interface StorageService<P : MinixPlugin> : ExtensionSkeleton<P> {
    val managedTable: Table

    @ApiStatus.NonExtendable
    override suspend fun handleUnload() {
        this.getProperty<HikariDataSource>("dataSource")?.close()
        this.deleteProperty("dataSource")
        this.deleteProperty("database")
    }

    @ApiStatus.NonExtendable
    suspend fun getDatabase(): Database? {
        this.ensureSetup()
        return this.getProperty("database")
    }

    fun getStorageDirectory(): Path = plugin.dataFolder.toPath()

    fun getDataSourceProperties(): Map<String, Any> = mapOf(
        "cachePrepStmts" to true,
        "prepStmtCacheSize" to 250,
        "prepStmtCacheSqlLimit" to 2048
    )

    private fun createDatabaseConfig() {
        with(HikariDataSource()) {
            this.jdbcUrl = "jdbc:sqlite:${getStorageDirectory()}/database.db"

            this@StorageService.getDataSourceProperties().forEach { (key, value) ->
                this.addDataSourceProperty(key, value)
            }

            setProperty("dataSource", this)
        }
    }

    private suspend fun ensureSetup() {
        val database = this.getProperty<Database>("database")
        if (database != null) return

        this.createDatabaseConfig()
        this.setProperty("database", Database.connect(this.getProperty<DataSource>("dataSource")!!, databaseConfig = databaseConfig))

        withDatabase {
            SchemaUtils.create(managedTable)
            SchemaUtils.addMissingColumnsStatements(managedTable)
        }
    }

    suspend fun <T> withDatabase(
        block: suspend Transaction.() -> T
    ): T = newSuspendedTransaction(this.dispatcher.get(), this.getDatabase()) {
        block()
    }

    suspend fun <T> withDatabaseAsync(
        block: suspend Transaction.() -> T
    ): Deferred<T> = suspendedTransactionAsync(this.dispatcher.get(), this.getDatabase()) {
        block()
    }

    private companion object {
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
