package app

import app.common.Configuration
import app.db.{Db, DbMigrator}
import io.helidon.webserver.WebServer
import sttp.tapir.server.nima.NimaServerInterpreter

object Main extends App {
  val config = Configuration.load()
  // Apply database migrations
  val dbMigrator = new DbMigrator(Db.pgDataSource)
  dbMigrator.migrate()

  val handlers = NimaServerInterpreter().toHandler(Endpoints.all)

  // Create and start the Nima server
  val server = WebServer.builder()
    .routing(builder => builder.any(handlers))
    .port(8080)
    .build()
    .start()

  println(s"WEB server is up at http://localhost:${server.port()}")
}
