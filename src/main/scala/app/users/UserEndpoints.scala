package app.users

import io.circe.generic.auto.*
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.netty.loom.Id

object UserEndpoints:
  val register: PublicEndpoint[UserRegisterData, Unit, String, Any] = endpoint.post
    .in("new" / "user")
    .in(formBody[UserRegisterData])
    .out(stringBody)
    .out(header("Content-Type", "text/html"))
    .out(header("HX-Trigger","newUser"))
  val registerEndpoint: ServerEndpoint[Any, Id] = register.serverLogicSuccess(user => {
    val userService = new UserService(new UserRepository())
    userService.register(user)
    html.new_user_button().toString()
  })

  val deleteUserEndpoint: PublicEndpoint[String, Unit, Unit, Any] = endpoint.delete
    .in("remove" / "user" / path[String]("username"))
  val deleteUserServerEndpoint: ServerEndpoint[Any, Id] = deleteUserEndpoint.serverLogicSuccess(username => {
    val userService = new UserService(new UserRepository())
    userService.delete(username)
    () // return nothing
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

  val indexEndpoint: PublicEndpoint[Unit, Unit, String, Any] = endpoint.get
    .in("users" / "all")
    .out(stringBody)
    .out(header("Content-Type", "text/html"))
  val indexServerEndpoint: ServerEndpoint[Any, Id] = indexEndpoint.serverLogicSuccess { _ =>
    val userService = new UserService(new UserRepository())
    val renderedHtml = html.users(userService.getAllUsers()).toString()
    renderedHtml
  }

  val userListEndpoint: PublicEndpoint[Unit, Unit, String, Any] = endpoint.get
    .in("users" / "all" / "list")
    .out(stringBody)
    .out(header("Content-Type", "text/html"))
  val userListServerEndpoint: ServerEndpoint[Any, Id] = userListEndpoint.serverLogicSuccess { _ =>
    val userService = new UserService(new UserRepository())
    val renderedHtml = html.users_list(userService.getAllUsers()).toString()
    renderedHtml
  }

  val getNewUserEndpoint: PublicEndpoint[Unit, Unit, String, Any] = endpoint.get
    .in("users" / "new" / "user")
    .out(stringBody)
    .out(header("Content-Type", "text/html"))
  val getNewUserServerEndpoint: ServerEndpoint[Any, Id] = getNewUserEndpoint.serverLogicSuccess { _ =>
    html.new_user_form().toString()

  }

  val getEditUserEndpoint: PublicEndpoint[String, Unit, String, Any] = endpoint.get
    .in("users" / path[String]("username") / "edit")
    .out(stringBody)
    .out(header("Content-Type", "text/html"))
  val getEditUserServerEndpoint: ServerEndpoint[Any, Id] = getEditUserEndpoint.serverLogicSuccess { username =>
    val userService = new UserService(new UserRepository()) // Initialize service and repository
    val foundUser = userService.findByUsername(username)
    foundUser match {
      case Some(user) => html.user_edit_form(user).toString()
      case None => html.not_found().toString()
    }
  }

  val getUserByUsernameEndpoint: PublicEndpoint[String, Unit, String, Any] = endpoint.get
    .in("users" / path[String]("username"))
    .out(stringBody)
    .out(header("Content-Type", "text/html"))
  val getUserByUsernameServerEndpoint: ServerEndpoint[Any, Id] = getUserByUsernameEndpoint.serverLogicSuccess { username =>
    val userService = new UserService(new UserRepository()) // Initialize service and repository
    val foundUser = userService.findByUsername(username)
    foundUser match {
      case Some(user) => html.user_details(user).toString()
      case None => html.not_found().toString()
    }
  }

  val getUserPartialEndpoint: PublicEndpoint[String, Unit, String, Any] = endpoint.get
    .in("users" / path[String]("username") / "partial")
    .out(stringBody)
    .out(header("Content-Type", "text/html"))
  val getUserPartialServerEndpoint: ServerEndpoint[Any, Id] = getUserPartialEndpoint.serverLogicSuccess { username =>
    val userService = new UserService(new UserRepository()) // Initialize service and repository
    val foundUser = userService.findByUsername(username)
    foundUser match {
      case Some(user) => html.user_partial(user).toString()
      case None => html.not_found().toString()
    }
  }

  val putEditUserEndpoint: PublicEndpoint[(String, UserUpdateData), Unit, String, Any] = endpoint.put
    .in("users" / path[String]("username") / "edit")
    .in(formBody[UserUpdateData])
    .out(stringBody)
    .out(header("Content-Type", "text/html"))
  val putEditUserServerEndpoint: ServerEndpoint[Any, Id] = putEditUserEndpoint.serverLogicSuccess { (username, updateData) =>
    val userService = new UserService(new UserRepository()) // Initialize service and repository
    val foundUser = userService.update(updateData, username)
    foundUser match {
      case Right(user) => html.user_partial(user).toString()
      case Left(UserNotFoundError(_)) => html.not_found().toString()
    }
  }


  val all = List(registerEndpoint, findByEmailEndpoint) ++ List(indexServerEndpoint, getEditUserServerEndpoint, userListServerEndpoint,
    getUserByUsernameServerEndpoint, putEditUserServerEndpoint, getUserPartialServerEndpoint, getNewUserServerEndpoint, deleteUserServerEndpoint)
