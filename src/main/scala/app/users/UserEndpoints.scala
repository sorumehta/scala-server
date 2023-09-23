package app.users

import io.circe.generic.auto.*
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.netty.loom.Id

object UserEndpoints:
  val register: PublicEndpoint[UserRegisterData, Unit, UserResponse, Any] = endpoint.post
    .in("api" / "users")
    .in(jsonBody[UserRegisterData])
    .out(jsonBody[UserResponse])
  val registerEndpoint: ServerEndpoint[Any, Id] = register.serverLogicSuccess(user => {
    val userService = new UserService(new UserRepository()) // Initialize service and repository
    userService.register(user)
  })

  // Endpoint to find a user by email
  val findByEmail: PublicEndpoint[String, Unit, Option[User], Any] = endpoint.get
    .in("api" / "users")
    .in(query[String]("email"))
    .out(jsonBody[Option[User]])

  val findByEmailEndpoint: ServerEndpoint[Any, Id] = findByEmail.serverLogicSuccess(email => {
    val userService = new UserService(new UserRepository()) // Initialize service and repository
    val foundUser = userService.findByEmail(email)
    foundUser // Return the found user or None
  })

  val all = List(registerEndpoint, findByEmailEndpoint)
