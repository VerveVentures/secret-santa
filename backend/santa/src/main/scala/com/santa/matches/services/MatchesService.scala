package com.santa.matches.services

import cats.effect.IO
import com.santa.matches.models.{CreateMatchInput, Match, MatchNotFoundError}
import com.santa.matches.repositories.MatchesRepository

import java.util.UUID

trait MatchesService {

  def create(input: CreateMatchInput): IO[Match]

  def getMatches(sessionId: String): IO[List[Match]]

  def get(id: String): IO[Either[MatchNotFoundError.type, Match]]

  def deleteMatch(id: String): IO[Either[MatchNotFoundError.type, Boolean]]
}

class MatchesServiceImpl(
  matchesRepository: MatchesRepository
) extends MatchesService {

  override def create(input: CreateMatchInput): IO[Match] = {
    matchesRepository.create(Match(
      id = UUID.randomUUID().toString,
      sessionId = input.sessionId,
      giver = input.giver,
      receiver = input.receiver
    ))
  }

  override def getMatches(sessionId: String): IO[List[Match]] = {
    matchesRepository.getMatches(sessionId)
  }

  override def get(id: String): IO[Either[MatchNotFoundError.type, Match]] = {
    matchesRepository.get(id)
  }

  override def deleteMatch(id: String): IO[Either[MatchNotFoundError.type, Boolean]] = {
    matchesRepository.deleteMatch(id)
  }
}
