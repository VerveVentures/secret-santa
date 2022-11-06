package com.santa.sessions.repositories

import cats.effect.IO
import com.santa.sessions.models.{Session, SessionNotFoundError}

trait SessionsRepository {

  def create(input: Session): IO[Session]

  def getSessions: IO[List[Session]]

  def get(id: String): IO[Either[SessionNotFoundError.type, Session]]

  def deleteSession(id: String): IO[Either[SessionNotFoundError.type, Boolean]]

  def updateSession(id: String, session: Session): IO[Either[SessionNotFoundError.type, Session]]

}


