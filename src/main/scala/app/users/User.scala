package app.users


import sttp.tapir.{Schema, SchemaType}
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.*

case class Email(value: String) extends AnyVal
case class User(
                 email: Email,
                 password: String,
                 username: String,
                 bio: Option[String],
                 image: Option[String]
               )

object User:
  given userDataEncoder: Encoder[User] = deriveEncoder[User]
  given userDataDecoder: Decoder[User] = deriveDecoder[User]

object Email:
  given emailEncoder: Encoder[Email] = Encoder.encodeString.contramap(_.value)
  given emailDecoder: Decoder[Email] = Decoder.decodeString.map(Email(_))
  given emailSchema: Schema[Email] = Schema(SchemaType.SString())
