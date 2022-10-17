package com.santa.matches.repositories

import com.santa.matches.models.Match


trait MatchesRepository {

  def create(input: Match): Match

  def getMatches(sessionId: String): List[Match]

  def get(id: String): Match

  def deleteMatch(id: String): Boolean
}


