package com.santa.sessions.controllers

import cats.effect._
import cats.implicits._
import com.santa.sessions.models.CreateSessionInput
import com.santa.sessions.repositories.{InMemorySessionsRepository, SessionsRepository}
import com.santa.sessions.services.{SessionsService, SessionsServiceImpl}
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl._

object SessionsController {

  val sessionsRepository: SessionsRepository = new InMemorySessionsRepository()
  val sessionsService: SessionsService = new SessionsServiceImpl(sessionsRepository)



  def sessionRoutes[F[_] : Concurrent]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "sessions" => {
        Ok(sessionsService.getSessions.asJson)
      }
      case req@POST -> Root / "sessions" =>
        implicit val sessionsDecoder: EntityDecoder[F, CreateSessionInput] = jsonOf[F, CreateSessionInput]
        for {
          createSessionInput <- req.as[CreateSessionInput]
          participant = sessionsService.create(createSessionInput)
          res <- Ok(participant.asJson)
        } yield {
          println("Created it", participant)
          res
        }
    }
  }
}
