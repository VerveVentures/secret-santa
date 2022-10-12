package com.santa.participants.controllers

import cats._
import cats.effect._
import cats.implicits._
import com.santa.participants.models.{CreateParticipantInput, Participant}
import com.santa.participants.repositories.{InMemoryParticipantRepository, ParticipantsRepository}
import com.santa.participants.services.{ParticipantsService, ParticipantsServiceImpl}
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

object ParticipantsController {

  val participantsRepository: ParticipantsRepository = new InMemoryParticipantRepository()
  val participantsService: ParticipantsService = new ParticipantsServiceImpl(participantsRepository)

  def participantRoutes[F[_] : Concurrent]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "participants" => {
        Ok()
      }
      case req@POST -> Root / "participants" =>
        implicit val participantDecoder: EntityDecoder[F, CreateParticipantInput] = jsonOf[F, CreateParticipantInput]
        for {
          participantInput <- req.as[CreateParticipantInput]
          participant = participantsService.create(participantInput)
          res <- Ok(participant.asJson)
        } yield {
          println("Created it", participant)
          res
        }
    }
  }
}
