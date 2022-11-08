package com.santa.sessions.services

import cats.effect.IO
import com.santa.sessions.models.{CreateSessionInput, Session, SessionNotFoundError, UpdateSessionInput}
import com.santa.sessions.repositories.SessionsRepository

import java.util.UUID

trait SessionsService {

  def create(input: CreateSessionInput): IO[Session]

  def getSessions: IO[List[Session]]

  def get(sessionId: String): IO[Either[SessionNotFoundError.type, Session]]

  def deleteSession(id: String): IO[Either[SessionNotFoundError.type, Boolean]]

  def updateSession(id: String, updateInput: UpdateSessionInput): IO[Either[SessionNotFoundError.type, Session]]
}

class SessionsServiceImpl(
  sessionRepository: SessionsRepository
) extends SessionsService {

  override def create(input: CreateSessionInput): IO[Session] = {
    sessionRepository.create(Session(
      id = UUID.randomUUID().toString,
      name = input.name,
      passphrase = input.passphrase,
      sessionScrambled = false, emailsSent = false
    ))
  }

  override def getSessions: IO[List[Session]] = {
    sessionRepository.getSessions
  }

  override def get(id: String): IO[Either[SessionNotFoundError.type, Session]] = {
    sessionRepository.get(id)
  }

  override def deleteSession(id: String): IO[Either[SessionNotFoundError.type, Boolean]] = {
    sessionRepository.deleteSession(id)
  }

  override def updateSession(id: String, updateInput: UpdateSessionInput): IO[Either[SessionNotFoundError.type, Session]] = {
    sessionRepository.get(id).flatMap({
      case Right(a) => sessionRepository.updateSession(id, a.copy(
        name = updateInput.name.getOrElse(a.name),
        sessionScrambled = updateInput.sessionScrambled.getOrElse(a.sessionScrambled),
        emailsSent = updateInput.emailsSent.getOrElse(a.emailsSent)
      ))
      case Left(_) => IO(Left(SessionNotFoundError))
    })
  }
}
