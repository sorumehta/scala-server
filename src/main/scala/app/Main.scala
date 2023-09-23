package app

import app.common.Configuration
import app.db.{Db, DbMigrator}
import sttp.tapir.*
import sttp.tapir.server.netty.loom.NettyIdServer

object Main extends App {
  val config = Configuration.load()
  // Apply database migrations
  val dbMigrator = new DbMigrator(Db.pgDataSource)
  dbMigrator.migrate()

  // Create and start the Nima server
  val server = NettyIdServer().addEndpoints(Endpoints.all).port(8080).start()

  println(s"WEB server is up at http://localhost:${server.port}")
}
