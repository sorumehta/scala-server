package app.users


import sttp.tapir.Schema.annotations.{validate, validateEach}
import sttp.tapir.Validator
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class UserRegisterData(
                             @validate(Validator.nonEmptyString) email: String,
                             @validate(Validator.minLength(3)) username: String,
                             @validate(Validator.nonEmptyString) password: String,
                             @validateEach(Validator.nonEmptyString) bio: Option[String]
                           )


object UserRegisterData {
  given userRegisterDataEncoder: Encoder[UserRegisterData] = deriveEncoder[UserRegisterData]
  given userRegisterDataDecoder: Decoder[UserRegisterData] = deriveDecoder[UserRegisterData]
}
