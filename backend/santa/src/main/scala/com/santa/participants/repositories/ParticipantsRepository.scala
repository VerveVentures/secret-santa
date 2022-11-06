package com.santa.participants.repositories

import cats.effect.IO
import com.santa.participants.models.{Participant, ParticipantNotFoundError}

trait ParticipantsRepository {

  def create(input: Participant): IO[Participant]

  def getParticipants(sessionId: String): IO[List[Participant]]

  def getParticipant(id: String): IO[Either[ParticipantNotFoundError.type, Participant]]

  def deleteParticipant(id: String): IO[Either[ParticipantNotFoundError.type, Boolean]]

  def updateParticipant(id: String, participant: Participant): IO[Either[ParticipantNotFoundError.type, Participant]]

}


