package com.santa.participants.controllers

import cats.effect._
import com.santa.participants.models.{CreateParticipantInput, UpdateParticipantInput}
import com.santa.participants.services.ParticipantsService
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl._

class ParticipantsController(participantsService: ParticipantsService) {

  def participantRoutes: HttpRoutes[IO] = {
    val dsl = Http4sDsl[IO]
    import dsl._

    HttpRoutes.of[IO] {
      case GET -> Root / "participants" / "session" /UUIDVar(sessionId) => {
        for {
          result <- participantsService.getParticipants(sessionId.toString)
          result <- Ok(result.asJson)
        } yield {
          result
        }
      }
      case GET -> Root / "participants" / UUIDVar(participantId) => {
        for {
          result <- participantsService.getParticipant(participantId.toString)
          result <- result match {
            case Left(_) => Ok(s"Participant with id ${participantId} could not found!")
            case Right(value) => Ok(value.asJson)
          }
        } yield {
          result
        }
      }
      case req@POST -> Root / "participants" =>
        implicit val participantDecoder: EntityDecoder[IO, CreateParticipantInput] = jsonOf[IO, CreateParticipantInput]
        for {
          participantInput <- req.as[CreateParticipantInput]
          participant <- participantsService.create(participantInput)
          res <- Ok(participant.asJson)
        } yield {
          res
        }
      case req@PUT -> Root / "participants" / UUIDVar(participantId) =>
        implicit val participantDecoder: EntityDecoder[IO, UpdateParticipantInput] = jsonOf[IO, UpdateParticipantInput]
        for {
          participantInput <- req.as[UpdateParticipantInput]
          participant <- participantsService.updateParticipant(participantId.toString, participantInput)
          res <- Ok(participant.asJson)
        } yield {
          res
        }
    }
  }
}
