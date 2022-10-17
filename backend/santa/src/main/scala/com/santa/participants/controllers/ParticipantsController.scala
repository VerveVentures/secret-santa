package com.santa.participants.controllers

import cats.effect._
import cats.implicits._
import com.santa.participants.models.CreateParticipantInput
import com.santa.participants.repositories.{InMemoryParticipantRepository, ParticipantsRepository}
import com.santa.participants.services.{ParticipantsService, ParticipantsServiceImpl}
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl._

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
