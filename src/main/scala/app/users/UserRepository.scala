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

  def getAllUsers(): List[User] = {
    val users = run(queryUser)
    users.map(userRowToUser)

  }
  def findByEmail(email: String): Option[User] = {
    val results = run(queryUser.filter(_.email == lift(email)))
    results.headOption.map(userRowToUser)
  }

  def findByUsername(username: String): Option[User] = {
    val results = run(queryUser.filter(_.username == lift(username)))
    results.headOption.map(userRowToUser)
  }

  def insert(user: UserRegisterData): Unit = {
    run(queryUser.insert(_.email -> lift(user.email), _.username -> lift(user.username), _.password -> lift(user.password)))
  }

  def updateByUsername(updateData: UserUpdateData, username: String): User = {

    val update = queryUser.dynamic
      .filter(_.username == lift(username))
      .update(
        setOpt[UserRow, String](_.email, updateData.email),
        setOpt[UserRow, String](_.password, updateData.password),
        setOpt[UserRow, String](_.bio.orNull, updateData.bio),
        setOpt[UserRow, String](_.image.orNull, updateData.image)
      )
    val read = quote(
      queryUser
        .filter(_.username == lift(username))
        .value
    )

    run(update)
    val user = run(read).head
    userRowToUser(user)

  }
}
