package com.santa.matches.repositories

import cats.effect.IO
import com.santa.matches.models.{Match, MatchNotFoundError}


trait MatchesRepository {

  def create(input: Match): IO[Match]

  def getMatches(sessionId: String): IO[List[Match]]

  def get(id: String): IO[Either[MatchNotFoundError.type, Match]]

  def deleteMatch(id: String): IO[Either[MatchNotFoundError.type, Boolean]]
}


