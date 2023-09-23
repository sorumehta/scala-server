package app

import sttp.tapir.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.netty.loom.Id
import html.index
import app.users.UserEndpoints


object Endpoints:
  val indexEndpoint: PublicEndpoint[Unit, Unit, String, Any] = endpoint.get
    .in("index" / "hello")
    .out(stringBody)
    .out(header("Content-Type", "text/html"))

  val indexRoute: ServerEndpoint[Any, Id] = indexEndpoint.serverLogicSuccess { _ =>
    val renderedHtml = html.index("Hello Soru boi!").toString()
    renderedHtml
  }

  val all = UserEndpoints.all ++ List(indexRoute)
