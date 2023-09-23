package app.db

import org.flywaydb.core.Flyway

import javax.sql.DataSource

class DbMigrator(ds: DataSource) {
  def migrate(): Unit = {
    val flyway = Flyway
      .configure()
      .dataSource(ds)
      .load()
    flyway.migrate()
  }
}