package app.users


import app.db.Db
import io.getquill.*
import sttp.tapir.Schema.annotations.validate
import sttp.tapir.Validator

case class UserRow(
                    userId: Int,
                    email: String,
                    username: String,
                    password: String,
                    bio: Option[String],
                    image: Option[String]
                  )


class UserRepository {

  import Db.ctx._

  private inline def queryUser = quote(querySchema[UserRow](entity = "users"))

  def userRowToUser(userRow: UserRow): User = {
    User(email = Email(userRow.email), password = userRow.password, username = userRow.username, bio = userRow.bio, image = userRow.image)
  }

  def findByEmail(email: String): Option[User] = {
    val results = run(queryUser.filter(_.email == lift(email)))
    results.headOption.map(userRowToUser)
  }

  def insert(user: UserRegisterData): Unit = {
    run(queryUser.insert(
      _.email -> lift(user.email),
      _.username -> lift(user.username),
      _.password -> lift(user.password)))
  }
}

