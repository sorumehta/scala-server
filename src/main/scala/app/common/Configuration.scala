package app.common

import app.db.DbConfig
import com.typesafe.config.ConfigFactory

case class Configuration(server: ServerConfig, db: DbConfig)

object Configuration {
  def load(): Configuration = {
    val config = ConfigFactory.load()
    val serverConfig = ServerConfig(
      host = config.getString("config.server.host"),
      port = config.getInt("config.server.port")
    )
    val dbConfig = DbConfig(
      user = config.getString("config.db.user"),
      password = config.getString("config.db.password"),
      database = config.getString("config.db.database"),
      server = config.getString("config.db.server"),
      port = config.getInt("config.db.port"),
      dataSource = config.getString("config.db.dataSource"),
    )
    Configuration(serverConfig, dbConfig)
  }
}

case class ServerConfig(host: String, port: Int)
