package com.santa.matches.repositories

import com.santa.matches.models.Match

class InMemoryMatchesRepository extends MatchesRepository {

  var database: Map[String, Match] = Map()

  override def create(input: Match): Match = {
    database += (input.id -> input)
    database(input.id)
  }

  override def getMatches(sessionId: String): List[Match] = {
    database.values.filter(_.sessionId == sessionId).toList
  }

  override def get(id: String): Match = {
    database(id)
  }

  override def deleteMatch(id: String): Boolean = {
    database -= id
    true
  }
}
