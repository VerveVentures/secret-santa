package com.santa.matches.services

import com.santa.matches.models.{CreateMatchInput, Match}
import com.santa.matches.repositories.MatchesRepository

import java.util.UUID

trait MatchesService {

  def create(input: CreateMatchInput): Match

  def getMatches(sessionId: String): List[Match]

  def get(id: String): Match

  def deleteMatch(id: String): Boolean
}

class MatchesServiceImpl(
  matchesRepository: MatchesRepository
) extends MatchesService {

  override def create(input: CreateMatchInput): Match = {
    matchesRepository.create(Match(
      id = UUID.randomUUID().toString,
      sessionId = input.sessionId,
      giver = input.giver,
      receiver = input.receiver
    ))
  }

  override def getMatches(sessionId: String): List[Match] = {
    matchesRepository.getMatches(sessionId)
  }

  override def get(id: String): Match = {
    matchesRepository.get(id)
  }

  override def deleteMatch(id: String): Boolean = {
    matchesRepository.deleteMatch(id)
  }
}
