package com.santa.sessions.repositories

import com.santa.sessions.models.Session


class InMemorySessionsRepository extends SessionsRepository {

  var database: Map[String, Session] = Map()

  override def create(session: Session): Session = {
    database += (session.id -> session)
    database(session.id)
  }

  override def getSessions(): List[Session] = {
    database.values.toList
  }

  override def deleteSession(sessionId: String): Boolean = {
    database = database - sessionId
    true
  }

  override def updateSession(id: String, session: Session): Session = {
    database += (id -> session)
    database(session.id)
  }

  override def get(id: String): Session = {
    database(id)
  }
}
