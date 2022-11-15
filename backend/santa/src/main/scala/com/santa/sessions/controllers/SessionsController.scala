package com.santa.sessions.controllers

import cats.effect._
import com.santa.sessions.models.{CreateSessionInput, UpdateSessionInput}
import com.santa.sessions.services.SessionsService
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl._

class SessionsController(sessionsService: SessionsService) {

  def sessionRoutes: HttpRoutes[IO] = {
    val dsl = Http4sDsl[IO]
    import dsl._

    HttpRoutes.of[IO] {
      case GET -> Root / "sessions" / UUIDVar(sessionId) => {
        for {
          result <- sessionsService.get(sessionId.toString)
          result <- result match {
            case Left(_) => Ok(s"Session with id ${sessionId} could not found!")
            case Right(value) => Ok(value.asJson)
          }
        } yield {
          result
        }
      }
      case POST -> Root / "sessions" / UUIDVar(sessionId) / "scramble" => {
        for {
          result <- sessionsService.scramble(sessionId.toString)
          result <- Ok(result.asJson)
        } yield {
          result
        }
      }
      case POST -> Root / "sessions" / UUIDVar(sessionId) / "launch" => {
        for {
          result <- sessionsService.launch(sessionId.toString)
          result <- Ok(result.asJson)
        } yield {
          result
        }
      }
      case POST -> Root / "sessions" / UUIDVar(sessionId) / "finalize" => {
        for {
          result <- sessionsService.sendOutcomes(sessionId.toString)
          result <- Ok(result.asJson)
        } yield {
          result
        }
      }
      case req@POST -> Root / "sessions" =>
        implicit val participantDecoder: EntityDecoder[IO, CreateSessionInput] = jsonOf[IO, CreateSessionInput]
        for {
          sessionInput <- req.as[CreateSessionInput]
          session <- sessionsService.create(sessionInput)
          res <- Ok(session.asJson)
        } yield {
          res
        }
      case req@PUT -> Root / "sessions" / UUIDVar(sessionId) =>
        implicit val participantDecoder: EntityDecoder[IO, UpdateSessionInput] = jsonOf[IO, UpdateSessionInput]
        for {
          sessionInput <- req.as[UpdateSessionInput]
          session <- sessionsService.updateSession(sessionId.toString, sessionInput)
          res <- Ok(session.asJson)
        } yield {
          res
        }
    }
  }
}
