package app.db

import app.common.Configuration
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import io.getquill.*

case class DbConfig(user: String, password: String, database: String, server: String, port: Int, dataSource: String)

object Db {
  val dbConfig: DbConfig = Configuration.load().db
  val pgDataSource = org.postgresql.ds.PGSimpleDataSource()
  pgDataSource.setUser(dbConfig.user)
  pgDataSource.setPassword(dbConfig.password)
  pgDataSource.setServerNames(Array(dbConfig.server))
  pgDataSource.setPortNumbers(Array(dbConfig.port))
  pgDataSource.setDatabaseName(dbConfig.database)

  private val hikariConfig = new HikariConfig()
  hikariConfig.setDataSource(pgDataSource)
  val ctx = new PostgresJdbcContext(SnakeCase, new HikariDataSource(hikariConfig))
}
