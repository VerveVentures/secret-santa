package com.santa.sessions.repositories

import com.santa.sessions.models.Session

trait SessionsRepository {

  def create(input: Session): Session

  def getSessions: List[Session]

  def get(id: String): Session

  def deleteSession(id: String): Boolean

  def updateSession(id: String, session: Session): Session

}


