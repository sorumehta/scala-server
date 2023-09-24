package app

import app.db.{Db, DbMigrator}
import sttp.tapir.*
import sttp.tapir.files.staticFilesGetServerEndpoint
import sttp.tapir.server.netty.loom.{Id, NettyIdServer}

object Main extends App {
  // Apply database migrations
  val dbMigrator = new DbMigrator(Db.pgDataSource)
  dbMigrator.migrate()

  // Create and start the server
  val server = NettyIdServer().addEndpoint(staticFilesGetServerEndpoint[Id]("images")("/Users/saurabhmehta/Personal/Dev/tapir-loom-server/static/images"))
    .addEndpoints(Endpoints.all).port(8080).start()

  println(s"WEB server is up at http://localhost:${server.port}")
}
