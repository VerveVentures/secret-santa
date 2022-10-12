package com.santa.participants.repositories

import com.santa.participants.models.Participant

trait ParticipantsRepository {

  def create(input: Participant): Participant

  def getParticipants(sessionId: String): List[Participant]

  def getParticipant(id: String): Participant

  def deleteParticipant(id: String): Boolean

  def updateParticipant(id: String, participant: Participant): Participant

}


