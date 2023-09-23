package app

import sttp.tapir.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.netty.loom.Id
import app.users.UserEndpoints


object Endpoints:

  val all = UserEndpoints.all
