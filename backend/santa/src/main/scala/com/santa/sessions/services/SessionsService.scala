package com.santa.sessions.services

import com.santa.sessions.models.{CreateSessionInput, Session, UpdateSessionInput}
import com.santa.sessions.repositories.SessionsRepository

import java.util.UUID

trait SessionsService {

  def create(input: CreateSessionInput): Session

  def getSessions: List[Session]

  def get(sessionId: String): Session

  def deleteSession(id: String): Boolean

  def updateSession(id: String, updateInput: UpdateSessionInput): Session
}

class SessionsServiceImpl(
  SessionRepository: SessionsRepository
) extends SessionsService {

  override def create(input: CreateSessionInput): Session = {
    SessionRepository.create(Session(
      id = UUID.randomUUID().toString,
      name = input.name,
      passphrase = input.passphrase,
      sessionScrambled = false, emailsSent = false
    ))
  }

  override def getSessions: List[Session] = {
    SessionRepository.getSessions
  }

  override def get(id: String): Session = {
    SessionRepository.get(id)
  }

  override def deleteSession(id: String): Boolean = {
    SessionRepository.deleteSession(id)
  }

  override def updateSession(id: String, updateInput: UpdateSessionInput): Session = {
    val Session = SessionRepository.get(id)
    SessionRepository.updateSession(id, Session.copy(name = updateInput.name))
  }
}
