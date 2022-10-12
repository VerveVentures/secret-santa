package com.santa.participants.services


import com.santa.participants.models.{CreateParticipantInput, Participant, UpdateParticipantInput}
import com.santa.participants.repositories.ParticipantsRepository

import java.util.UUID

trait ParticipantsService {

  def create(input: CreateParticipantInput): Participant

  def getParticipants(sessionId: String): List[Participant]

  def getParticipant(sessionId: String): Participant

  def deleteParticipant(id: String): Boolean

  def updateParticipant(id: String, updateInput: UpdateParticipantInput): Participant
}

class ParticipantsServiceImpl(
  participantRepository: ParticipantsRepository
) extends ParticipantsService {

  override def create(input: CreateParticipantInput): Participant = {
    participantRepository.create(Participant(
      id = UUID.randomUUID().toString,
      name = input.name,
      sessionId = input.sessionId
    ))
  }

  override def getParticipants(sessionId: String): List[Participant] = {
    participantRepository.getParticipants(sessionId)
  }

  override def getParticipant(id: String): Participant = {
    participantRepository.getParticipant(id)
  }

  override def deleteParticipant(id: String): Boolean = {
    participantRepository.deleteParticipant(id)
  }

  override def updateParticipant(id: String, updateInput: UpdateParticipantInput): Participant = {
    val participant = participantRepository.getParticipant(id)
    participantRepository.updateParticipant(id, participant.copy(name = updateInput.name))
  }
}
