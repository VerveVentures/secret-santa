package com.santa.participants.services


import cats.effect.IO
import com.santa.participants.models.{CreateParticipantInput, Participant, ParticipantNotFoundError, UpdateParticipantInput}
import com.santa.participants.repositories.ParticipantsRepository

import java.util.UUID

trait ParticipantsService {

  def create(input: CreateParticipantInput): IO[Participant]

  def getParticipants(sessionId: String): IO[List[Participant]]

  def getParticipant(sessionId: String): IO[Either[ParticipantNotFoundError.type, Participant]]

  def deleteParticipant(id: String): IO[Either[ParticipantNotFoundError.type, Boolean]]

  def updateParticipant(id: String, updateInput: UpdateParticipantInput):  IO[Either[ParticipantNotFoundError.type, Participant]]
}

class ParticipantsServiceImpl(
  participantRepository: ParticipantsRepository
) extends ParticipantsService {

  override def create(input: CreateParticipantInput): IO[Participant] = {
    participantRepository.create(Participant(
      id = UUID.randomUUID().toString,
      firstName = input.firstName,
      lastName = input.lastName,
      sessionId = input.sessionId,
      comment = input.comment,
      email = input.email,
      participates = input.participates
    ))
  }

  override def getParticipants(sessionId: String): IO[List[Participant]] = {
    participantRepository.getParticipants(sessionId)
  }

  override def getParticipant(id: String):  IO[Either[ParticipantNotFoundError.type, Participant]] = {
    participantRepository.getParticipant(id)
  }

  override def deleteParticipant(id: String):  IO[Either[ParticipantNotFoundError.type, Boolean]] = {
    participantRepository.deleteParticipant(id)
  }

  override def updateParticipant(id: String, updateInput: UpdateParticipantInput): IO[Either[ParticipantNotFoundError.type, Participant]] = {
    participantRepository.getParticipant(id).flatMap( {
        case Right(a) => participantRepository.updateParticipant(id, a.copy(
          firstName = updateInput.firstName.getOrElse(a.firstName),
          lastName = updateInput.lastName.getOrElse(a.lastName),
          email = updateInput.email.getOrElse(a.email),
          participates = updateInput.participates.orElse(a.participates),
          comment = updateInput.comment.orElse(a.comment)
        ))
        case Left(_) => IO(Left(ParticipantNotFoundError))
      }
    )
  }
}
