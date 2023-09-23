package app.users

import io.circe.generic.semiauto.{deriveEncoder, deriveDecoder}
import io.circe.{Decoder, Encoder}
import app.users.User

case class UserResponse(
                         user: User
                       )

object UserResponse:
  given userEncoder: Encoder[UserResponse] = deriveEncoder[UserResponse]
  given userDecoder: Decoder[UserResponse] = deriveDecoder[UserResponse]
