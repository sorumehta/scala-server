package app.users


import sttp.tapir.Schema.annotations.validateEach
import sttp.tapir.Validator
import io.circe.generic.semiauto.{deriveEncoder, deriveDecoder}
import io.circe.{Decoder, Encoder}

case class UserUpdateData(
                           @validateEach(Validator.nonEmptyString) email: Option[String],
                           @validateEach(Validator.minLength(3)) username: Option[String],
                           @validateEach(Validator.nonEmptyString) password: Option[String],
                           @validateEach(Validator.nonEmptyString) bio: Option[String],
                           @validateEach(Validator.nonEmptyString) image: Option[String]
                         ) {
  def update(userData: User): UserUpdateData =
    UserUpdateData(
      this.email.orElse(Some(userData.email.value)),
      this.username.orElse(Some(userData.username)),
      Some(userData.password),
      this.bio.orElse(userData.bio),
      this.image.orElse(userData.image)
    )
}

object UserUpdateData:
  def apply(
             email: Option[String],
             username: Option[String],
             password: Option[String],
             bio: Option[String],
             image: Option[String]
           ): UserUpdateData = {
    new UserUpdateData(
      email.map(_.toLowerCase.trim),
      username.map(_.trim),
      password,
      bio,
      image
    )
  }
  given userUpdateDataEncoder: Encoder[UserUpdateData] = deriveEncoder[UserUpdateData]
  given userUpdateDataDecoder: Decoder[UserUpdateData] = deriveDecoder[UserUpdateData]
