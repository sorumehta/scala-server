package app.users


import sttp.tapir.Schema.annotations.validate
import sttp.tapir.Validator
import io.circe.generic.semiauto.{deriveEncoder, deriveDecoder}
import io.circe.{Decoder, Encoder}

case class UserRegisterData(
                             @validate(Validator.nonEmptyString) email: String,
                             @validate(Validator.minLength(3)) username: String,
                             @validate(Validator.nonEmptyString) password: String
                           )


object UserRegisterData {
  given userRegisterDataEncoder: Encoder[UserRegisterData] = deriveEncoder[UserRegisterData]
  given userRegisterDataDecoder: Decoder[UserRegisterData] = deriveDecoder[UserRegisterData]
}
