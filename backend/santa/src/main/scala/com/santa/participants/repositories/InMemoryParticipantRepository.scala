package com.santa.participants.repositories
import com.santa.participants.models.{CreateParticipantInput, Participant, UpdateParticipantInput}

class InMemoryParticipantRepository extends ParticipantsRepository {

  var database: Map[String, Participant] = Map()

  override def create(participant: Participant): Participant = {
    database += (participant.id -> participant)
    participant
  }

  override def getParticipants(sessionId: String): List[Participant] = {
    database.values.filter(_.sessionId == sessionId).toList
  }

  override def deleteParticipant(participantId: String): Boolean = {
    database = database - participantId
    true
  }

  override def updateParticipant(id: String, participant: Participant): Participant = {
    database += (id -> participant)
    participant
  }

  override def getParticipant(id: String): Participant = {
    database(id)
  }
}
