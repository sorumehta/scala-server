package app.users

import app.users.{UserRegisterData, UserResponse}


class UserService(repository: UserRepository) {

  def findByEmail(email: String): Option[User] = {
    repository.findByEmail(email)
  }

  def register(user: UserRegisterData): UserResponse = {
    repository.insert(user)
    val createdUser = User(Email(user.email), user.password, user.username, bio = None, image = None)
    UserResponse(createdUser)
  }
}
