package com.santa.emails

import cats.effect.IO
import com.santa.config.config.MailConfig
import io.circe.Json
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps
import org.http4s._
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.client.Client
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.{Accept, Authorization}
import org.http4s.implicits.http4sLiteralsSyntax

class EmailsService(
   mailConfig: MailConfig,
   httpClient: Client[IO]
) {

  def sendRequest(emailRequest: EmailRequest): IO[EmailSendResponse] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._
    implicit val emailRequestEncoder: EntityEncoder[IO, Json] = jsonEncoderOf[IO, Json]
    implicit val emailSendResponseDecoder: EntityDecoder[IO, EmailSendResponse] = jsonOf[IO, EmailSendResponse]

    val request = Request[IO](method = POST, uri = uri"https://api.mailersend.com/v1/email", headers = Headers(
      Authorization(Credentials.Token(AuthScheme.Bearer, mailConfig.apiToken)),
      Accept(MediaType.application.json),
    )).withEntity(emailRequest.asJson)

    for {
      response <- httpClient.run(request).attempt.use({
        case Right(response) => IO(EmailSendResponse(response.status.code))
        case Left(throwable) => {
          println("Failed to send email", throwable.getMessage)
          throw throwable
        }
      })
    } yield {
      response
    }
  }
}
