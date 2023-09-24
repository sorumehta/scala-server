package app

import app.db.{Db, DbMigrator}
import com.typesafe.scalalogging.Logger
import sttp.tapir.*
import sttp.tapir.files.staticFilesGetServerEndpoint
import sttp.tapir.server.netty.loom.{Id, NettyIdServer}

object Main extends App {
  // Apply database migrations
  val logger = Logger(getClass.getName)
  val dbMigrator = new DbMigrator(Db.pgDataSource)
  dbMigrator.migrate()

  // Create and start the server
  val staticFilesPath = "/Users/saurabhmehta/Personal/Dev/tapir-loom-server/static/"
  val server = NettyIdServer()
    .addEndpoint(staticFilesGetServerEndpoint[Id]("static")(staticFilesPath))
    .addEndpoints(Endpoints.all).port(8080).start()
  
  logger.info(s"WEB server is up at http://localhost:${server.port}")
}
