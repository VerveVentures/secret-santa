package com.santa.sessions.controllers

import cats._
import cats.effect._
import cats.implicits._
import com.santa.participants.models.{CreateParticipantInput, Participant}
import com.santa.participants.repositories.{InMemoryParticipantRepository, ParticipantsRepository}
import com.santa.participants.services.{ParticipantsService, ParticipantsServiceImpl}
import com.santa.sessions.models.CreateSessionInput
import com.santa.sessions.repositories.{InMemorySessionsRepository, SessionsRepository}
import com.santa.sessions.services.{SessionsService, SessionsServiceImpl}
import org.http4s.circe._
import org.http4s._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.dsl._
import org.http4s.dsl.impl._
import org.http4s.headers._
import org.http4s.implicits._
import org.http4s.server._
import org.http4s.server.blaze.BlazeServerBuilder
import org.typelevel.ci.CIString

import java.time.Year
import java.util.UUID
import scala.collection.mutable
import scala.util.Try
import scala.concurrent.ExecutionContext.global

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
