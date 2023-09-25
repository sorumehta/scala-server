package app.users

import app.users.{UserRegisterData, UserResponse}

sealed trait Error
case class UserNotFoundError(username: String) extends Error {
  override def toString: String = s"User with username $username not found."
}

class UserService(repository: UserRepository) {

  def getAllUsers() = {
    repository.getAllUsers()
  }

  def findByEmail(email: String): Option[User] = {
    repository.findByEmail(email)
  }

  def findByUsername(username: String): Option[User] = {
    repository.findByUsername(username)
  }

  def register(user: UserRegisterData): UserResponse = {
    repository.insert(user)
    val createdUser = User(Email(user.email), user.password, user.username, bio = user.bio, image = None)
    UserResponse(createdUser)
  }

  def update(updateData: UserUpdateData, username: String): Either[Error, User] = {
    val emailCleanOpt = updateData.email.map(email => email.toLowerCase.trim())
    val oldUserOption = repository.findByUsername(username)

    oldUserOption match {
      case Some(oldUser) =>
        val updatedUser = updateData.update(oldUser)
        Right(repository.updateByUsername(updatedUser, username))

      case None =>
        Left(UserNotFoundError(username))
    }
  }
}
